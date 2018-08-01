package bgu.spl.a2;
import java.util.concurrent.ConcurrentLinkedDeque;
/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

    protected ConcurrentLinkedDeque<Task<?>>[] queueArr;
    protected Thread[] threadsArr;
    /**package**/ VersionMonitor verMonitor;
    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {

        queueArr = new ConcurrentLinkedDeque[nthreads];
        threadsArr = new Thread[nthreads];
        verMonitor = new VersionMonitor();

        for( int i = 0 ; i < nthreads ; i++){
            threadsArr[i] = new Thread(new Processor(i,this));
            queueArr[i]  = new ConcurrentLinkedDeque<>();
        }
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {

        int random = (int)(Math.random() * threadsArr.length);
        queueArr[random].addFirst(task);
        verMonitor.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {

        for (int i = 0; i < threadsArr.length; i++){
            if( Thread.currentThread() == threadsArr[i]) throw new UnsupportedOperationException("Can't shut down this workstealing threadpool");
            threadsArr[i].interrupt();

        }
    }
    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for(int i = 0 ; i < threadsArr.length ; i++ )
         threadsArr[i].start();
    }
}
