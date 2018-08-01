package bgu.spl.a2;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

    private AtomicInteger ver;

    /**
     * constructor
     */
    public VersionMonitor(){

        ver = new AtomicInteger(0);
    }

    /**
     * @return the current version
     */
    public int getVersion() {

        return ver.get();
    }

    /**
     * increase the version by one and than notify all the other threads about it
     */
    public void inc() {

        /*
        *we synchronized this part because we don't want two threads to increment the version
         *we want that one increment of the version will happen and than to wake up all the other threads
         */
        synchronized (this){

            ver.incrementAndGet();
            this.notifyAll();
        }
    }

    /**
     *
     * @param version
     * @throws InterruptedException
     *
     * the version monitor waits until the version changes as means if a new task has been added
     * or something has change in the pool
     * if a thread can now handle or steal other tasks the version will change and he will get out the wait method
     */
    public synchronized void await(int version) throws InterruptedException {

        if(version != getVersion()) {
            return;
        }
        else
            while(version == getVersion()){
                wait();
        }

    }
}
