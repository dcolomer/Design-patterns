//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;


import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadChain<T> implements Runnable {

    static int ThreadId = 0;
    public ThreadChain next;
    private ThreadPool threadpool; ///pointer to the parent thread pool
    private final Condition condWait;
    //private final Condition condthreadChk;
    private final Lock condLock;
    int m_ref = 0;

    int threadId = 0;
    boolean deleteMe;
    AtomicLong starttime = new AtomicLong(0);

    ;
    long m_timeout;
    long lastactivetime;
    boolean busy;
    Thread thisThread;

    //	/reference couting
    void AddRef() {
        ++m_ref;
    }

    public ThreadChain(ThreadChain p, ThreadPool pool, String name) {
        AddRef();
        // intialise in the constructor
        deleteMe = false;
        busy = false;
        next = p; //set the thread chain
        threadpool = pool; //set the thread pool

        condLock = new ReentrantLock();
        condWait = condLock.newCondition();

        threadId = ++ThreadId;
     
        System.out.println("ThreadChain - created a thread id=" + threadId);
        Integer inttid = threadId;
        pool.IncrementThreadCount();
        // start the thread
        thisThread = new Thread(this, name + inttid.toString());
        thisThread.start();
    }

    public ThreadChain(ThreadChain p, ThreadPool pool) {
        AddRef();
        // intialise in the constructor
        deleteMe = false;
        busy = false;
        next = p; //set the thread chain
        threadpool = pool; //set the thread pool

        condLock = new ReentrantLock();
        condWait = condLock.newCondition();

        threadId = ++ThreadId;
      
        System.out.println("ThreadChain - created a thread id=" + threadId);
        Integer inttid = threadId;
        pool.IncrementThreadCount();
        // start the thread
        thisThread = new Thread(this, "thrdpool"+inttid.toString());
        thisThread.start();

    }

    public Boolean canHandle() {

        if (!busy) {

            System.out.println("Can Handle This Event in id=" + threadId);
            // todo signal an event
            try {
                condLock.lock();
                condWait.signal();
                System.out.println("Thread Order 3");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                condLock.unlock();
            }
            return true;
        }
        if (busy)///if the associated thread is already processing a request
        {
            if (next == null) {
                ///Means there is no next thread so  check if threads should be added
                ///if the thread count is already max nothing to be done

                for (int i = (int) threadpool.m_threadCount; i <= threadpool.maxthreadCount; ++i) {
                    System.out.println("Generating a new thread  threadCount= " + threadpool.m_threadCount);   ///acp check this
                    next = new ThreadChain<T>(null, threadpool);///create a new thread
                    break; ///Only add one thread at a time
                }
            }
            ///If the thread count is already max nothing to be done
            if (next == null) {
                //printf("Max thread Count reacehed\n");
                return false; ///max thread count reached
            }

            ///Else see if the next obejct in the chain is free
            /// to handle the request
            return next.canHandle();
        }

        return false;
    }

//	This is the method that is called by the thread, it waits for an event
    /// note- exceptions thrown by the excecute method are not caught
    void HandleRequest() {
        //threadId = GetCurrentThreadId();
        System.out.println("In HandleRequest of thread id=" + threadId);

        //The deleteMe is set in the dtor usually if this threads time out is over
        while (!deleteMe) {
            System.out.println("Waiting for an event in Thread- id=" + ThreadId);
            try {
                //System.out.println("Thread Order 4 Wait 1");
                condLock.lock();
                //System.out.println("Thread Order 4 Wait 2");
                condWait.await(); ///This event is signalled from the canHandle mthd
                System.out.println("Thread Order 4");
                condLock.unlock();

                ///Set the start time - for checking if the 	thread is hang or not
                starttime.set(System.currentTimeMillis());
                System.out.println("Inside the Run method ThreadId- id=" + ThreadId);

                //Now set that this object as busy-it is going to handle a request
                busy = true;
                //printf("Thread %d is busy\n",threadId);
                ///printf("Handling a request in thread=%d\n",threadId);

                ///there is no need of syncronizhing this
                ///as only one thread will be calling GetRequest at a time
                Command<T> temp = threadpool.getRequest();
                System.out.println("Thread Order 5");
                if (temp != null) {
                    System.out.println("Thread Order 6");

                    System.out.println("Handling a request in thread id=" + threadId);
                    m_timeout = temp.GetTimeOut() * 1000; /// The time out is in seconds
                    temp.execute();
                    temp = null;

                }
                ///else
                ///printf("GetRequest gave null!!");

                System.out.println("Thread Order 7");
                ///Okay task is over - so set it to not busy state
                busy = false;
                //printf("Thread %d is NOT busy\n",threadId);

                ///Reset the start time
                starttime.set(0);
                // Now check if the minimum thread count is exceeded; then delete this thread also
                // so that there is no unnecessary max threads this has to be via last used time event
                lastactivetime = System.currentTimeMillis();

                ///Reset the event to non signalled state
                //nothing to do Java takes care

            }//end of try block
            catch (InterruptedException e) {
                // e.printStackTrace();
            }

        }///end of while(!deleteMe)

        ///We are here because the thread has taken more than the timeout value
        ///to exceute

        System.out.println("Thread Released/killed threaid=" + threadId);

        // This thread had finished
    }

    public void run() {
        HandleRequest();
    }

    ///Get the next object in the chain
    ThreadChain GetNext() {
        return next;
    }
    ///Method to check if the thread associated
    ///witht this thread is hung

    boolean IsHung() {
        ///printf("In IsHung method\n");


        //condthreadChk.await();
        if ((starttime.get() == 0) || (m_timeout == -1)) {
            return false;
        }

        long now = System.currentTimeMillis();
        if ((now - starttime.get()) > m_timeout) {
            System.out.println("Thread IsHung Id= " + threadId);
            return true;
        }

        return false;

    }

    ///The method to handle the hung threads
    void HandleHungThreads(ThreadChain prev) {
        //System.out.println("In HandleHungThreads");

        ThreadChain next_p = GetNext();
        boolean bIsHung = false;

        if (IsHung() || ReleaseIdleThread()) {
            System.out.println("In HandleHungThreads Thread id=" + threadId + "is hung/idle!!");
            bIsHung = true;
            if (this == threadpool.root) ///case if root is hanging
            {
                System.out.println("Root is hung");
                threadpool.root = next_p;
                prev = next_p;
            } else {
                prev.next = GetNext(); // reomove this item from thread chain link
            }
        }

        if (bIsHung)///if this is a hung thread that is waking up
        {
            Release();
        }

        if (next == null)///propogate to the next object
        {
            // System.out.println("Next is NULL, TC=" + threadpool.m_threadCount);
            return;
        }

        if (bIsHung) {
            //System.out.println("If this is hung next_p=" +next_p + "prev=" +prev);
            next.HandleHungThreads(prev);
        } else {
            next.HandleHungThreads(this);
        }
        ///printf("Out HandleHungThreads\n");

    }

    public void deleteMe() {
        deleteMe = true;
    }

    ///reference couting
    private void Release() {
        --m_ref;
        if (m_ref == 0) {
            threadpool.DecrementThreadCount();
            System.out.println("Deleting thread id=" + threadId
                    + " ThreadCount=" + threadpool.m_threadCount);
            deleteMe = true;
            this.thisThread.interrupt();
            //signal the object so that it comes out of the thread
            try {
                condLock.lock();
                condWait.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                condLock.unlock();
            }

        }
    }
//	To check if this thread is idle and can be released
    // Note - it is a prerequisite that this is called only after
    // checking if the thread has hung

    boolean ReleaseIdleThread() {
        boolean bReleasethis = false;
        try {

            //thread count is equal to the minimum thread count
            if (threadpool.m_threadCount <= threadpool.minthreadCount) {
                //threadChkMutex.unlock();
                return false;
            }

            //printf("Excess thread[%d] ThreadCount=%d\n",threadId,threadpool->m_threadCount);
            if (lastactivetime == 0) {
                //System.out.println("This thread was never executed");
                bReleasethis = false;
            } else if (System.currentTimeMillis() - lastactivetime > threadpool.MAX_IDLETIME) {
                System.out.println("Max time exceeded for this thread");
                bReleasethis = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //threadChkMutex.unlock();
        }
        return bReleasethis;

    }

    @Override
    protected void finalize() {
        deleteMe = true;
        condWait.notifyAll();
    }
}
