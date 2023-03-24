//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool<T> extends Object {

    static final int THREAD_CHECKTIME = 5; // / 1 second
    static final int MAX_WAITTIMEOUT = 10;// / 2 Seconds
    final long MAX_IDLETIME;// = 30 * 1000; // 5 minutes
    public ThreadChain root;
    // /std::queue<Command<T>*,std::list<Command<T>*> > m_RequestQueue;
    long m_nPendingCount;
    int minthreadCount;// the mimimum threads in thread pool
    int maxthreadCount;// the maximum thread count in threadpool
    volatile long m_threadCount; // the current dynamic thread count -
    // volatile for locked increments/decrements
    // HANDLE m_pending;
    // HANDLE freeThreadEvent;
    CRequestQueue<T> m_RequestQueue; // the container to hold the request
    // object
    private final Lock condLock;
    private final Condition m_pending;

    // /there is no meaning in copying a thread pool,
    // /so making copy ctor and copy assignment private
	/*	
    private ThreadPool(ThreadPool t) {
    };*/
    public ThreadPool(int _minthreadcount, int _maxthreadcount, long maxtimeout) {
        // Initialise the Threadpool with data
        root = null;
        minthreadCount = _minthreadcount;
        maxthreadCount = _maxthreadcount;
        m_nPendingCount = 0;
        m_threadCount = 0;
        condLock = new ReentrantLock();
        m_pending = condLock.newCondition();
        m_RequestQueue = new CRequestQueue<T>();
        // InitializeCriticalSection(&critsec);
        MAX_IDLETIME = maxtimeout;

        // /There is at least one thread in the thread pool
        ThreadChain prev = new ThreadChain(null, this, "root");// /last node
        root = prev;

        // /Initially only create the minimum threads needed
        for (int i = 0; i < minthreadCount - 1; ++i) {
            ThreadChain temp = new ThreadChain(prev, this);
            prev = temp;
            root = temp;
        }

        // /Create a thread that will monitor the threads to see if they are
        // hanging
        // ThreadCheckerProxy
        new Thread("IdleHungThrdChk") {

            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(THREAD_CHECKTIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }// /minimum time
                    if (root != null) {
                        root.HandleHungThreads(root);
                        //log.info("ThreadPool - HandleHungThread");
                    } 
                }
            }
        }.start();


        // /Create an event in the non signalled state
        // /which is used to check if there are any pending tasks
        //m_pending=CreateEvent(0,1,0,0); // /Manually reset

        // /Create a thread that will monitor the pending jobs


        new Thread("ConsumerThrd") {

            @Override
            public void run() {

                while (true) {
                    try {
                        //System.out.println("ThreadPool-Waiting for Task");
                        condLock.lock();//the queue is a resource // under contention
                        m_pending.await(MAX_WAITTIMEOUT, TimeUnit.NANOSECONDS);//or
                        // a maximum time interval
                        //System.out.println("Thread Order 2");
                        //System.out.println("ThreadPool-Task Received or Time out");

                        if (m_RequestQueue.empty()) {
                            System.out.println(".");// m_RequestQueue is
                            // Empty\n");
                            m_pending.await();// /If empty wait for the
                            // event to occur
                            condLock.unlock();
                        } else {

                            HandleRequest();// /Call to the ThreadChain method
                            // to handle a requesr
                            //log.info("m_RequestQueue is NOT Empty");
                            continue;
                            //Sleep(0);// / To avoid busy-looping
                        }
                    } catch (Exception e) {
                        System.out.println("Exception Caught in ProcessList");
                        e.printStackTrace();
                    } finally {
                        //condLock.unlock();
                    }

                }//while
            }// end method
        }.start();
    }

    // / This method increments the thread count of the thread pool
    // / in a thread safe manner
    void IncrementThreadCount() {
        ++m_threadCount;
    }

    // / This method decrements the thread count of the thread pool
    // / in a thread safe manner
    void DecrementThreadCount() {
        --m_threadCount;
    }

    ///New Method used to Queue the request in a list.
    ///This can be called from multiple threads
    void QueueRequest(Command<T> cmdPtr) throws Exception {
        try {
            if (cmdPtr != null) /// If this is a valid pointer
            {
                m_RequestQueue.queueRequest(cmdPtr);
                ///printf("Q Size=%d\n",m_RequestQueue.size());
                condLock.lock();
                m_pending.signal(); ///signal the ProcessList to process the
                System.out.println("Thread Order 1");
                condLock.unlock();
                return;
            }

        } catch (Exception e) {
            System.out.println("Exception Caught in QueueRequest.....");
            e.printStackTrace();
            throw e;
        }

    }

    // / This method is called by a thread when it is free to procees
    // /a request. It deques the request -fifo
    public Command<T> getRequest() {
        try {
            return m_RequestQueue.getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void HandleRequest() {
        /// todo put an assert here root should not be null
        ///This call chains through the ThreadChain objects,the first free object
        ///can handle the tasks
        if (root != null) {
            root.canHandle();
        } else {
            System.out.println("No ROOT!!");
            //can happen if all threads are hanging
            ///There is at least one thread in the thread pool
            ThreadChain prev = new ThreadChain(null, this, "root");///last node
            root = prev;
        }
        return;

    }
}
