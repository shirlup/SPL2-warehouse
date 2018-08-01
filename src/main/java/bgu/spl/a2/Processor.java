package bgu.spl.a2;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool workStealingThreadPool;
    private final int id;
    /**
     * package
     **/

    /**
     * constructor for this class
     * <p>
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     * <p>
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id   - the processor id (every processor need to have its own unique
     *             id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.workStealingThreadPool = pool;
    }

    /**
     * This method is how the processor runs
     */
    public void run() {
        //while the current thread is not being interrupt it can perform /steal tasks
        while(!Thread.currentThread().isInterrupted()){
            ConcurrentLinkedDeque<Task<?>> currentThreadQueue = workStealingThreadPool.queueArr[id];

            //if current processor has no tasks to perform in queue it needs to steal from another processor
            if(currentThreadQueue.isEmpty()){
                if(!stealFrom()){
                    try {
                        int ver = this.workStealingThreadPool.verMonitor.getVersion();
                        workStealingThreadPool.verMonitor.await(ver);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); //interrupt
                    }
                }
            }
            else{
                try{
                    Task<?> task = currentThreadQueue.removeFirst();
                    if(task != null){
                        task.handle(this); //handels the task from the queue
                    }
                }
                catch(NoSuchElementException e){
                    Thread.currentThread().interrupt(); //interrupt
                }
            }
        }
    }

        private boolean stealFrom(){

            int numberOfThreads = workStealingThreadPool.threadsArr.length;
            int size;
             AtomicBoolean toSteal = new AtomicBoolean(false);

            int toStealFrom = ( this.id+1 )%( numberOfThreads );
            while((toStealFrom!=this.id) && !(toSteal.get())){
                size = workStealingThreadPool.queueArr[toStealFrom].size();
                if(!workStealingThreadPool.queueArr[toStealFrom].isEmpty()){
                    for(int i=0 ; i <= toStealFrom/2 ; i++){
                        try {
                            Task stolenTask = workStealingThreadPool.queueArr[toStealFrom].pollLast();
                            if (stolenTask != null)
                                workStealingThreadPool.queueArr[this.id].addFirst(stolenTask);
                            toSteal.set(true);
                        }
                        catch (NoSuchElementException e){}
                    }
                }
                toStealFrom = (toStealFrom+1) % (numberOfThreads);
            }
            /*
            for(int toStealFrom=(this.id+1)%(numberOfThreads); toStealFrom!=this.id && !toSteal.get() ; toStealFrom= (toStealFrom+1)%(numberOfThreads)){
                size=workStealingThreadPool.queueArr[toStealFrom].size();
                if(!workStealingThreadPool.queueArr[toStealFrom].isEmpty()){
                    for(int i=0;i<=toStealFrom/2;i++){
                        try {
                            Task toSolve = workStealingThreadPool.queueArr[toStealFrom].pollLast();
                            if (toSolve!=null) workStealingThreadPool.queueArr[this.id].addFirst(tmpTask);
                            toSteal.set(true);
                        }
                        catch (NoSuchElementException e){}
                    }
                }

            }*/
            return toSteal.get();
        }


        /*
        while (!interrupted) {
            //Execute all the tasks from the queue
            Task toPreform = pool.queueArr[id].pollFirst();
            if (toPreform != null) {
                toPreform.handle(this);
            }
            //Stealing from another processor
            else {
                //select a victim
                int stealFrom = (id + 1) % (pool.queueArr.length);
                boolean successSteal = false;
                while (stealFrom != id) {
                    if (successSteal == false) {
                        int stealFromSize = pool.queueArr[stealFrom].size() / 2;

                        for (int i = 0; i < stealFromSize; i++) {
                            Task taskToSteal = pool.queueArr[stealFrom].pollLast();
                            if (taskToSteal != null) {
                                successSteal = true;
                                pool.queueArr[id].addLast(taskToSteal);
                            } else {
                                break;
                            }
                        }

                    }
                    stealFrom = (stealFrom + 1) % (pool.queueArr.length);
                }

                if (successSteal == false) {
                    toPreform = pool.queueArr[id].pollFirst();
                    if (toPreform != null) {
                        toPreform.handle(this);
                    } else {
                      /* try {
                            Thread.sleep(1);
                            pool.verMonitor.await(pool.verMonitor.getVersion());
                        } catch (InterruptedException e) {
                           interrupted = true;
                        }
                    }
                }
            }
        }*/


    /**
     * this method ascribes a new task to a processor's queue
     * @param task
     */
    protected void addTaskToQueue(Task<?> task) {
        workStealingThreadPool.queueArr[id].addLast(task);
    }
}