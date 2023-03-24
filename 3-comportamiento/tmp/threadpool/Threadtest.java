/**
 *  Class for testing the thread class
 */
package evolution.threads.threadpool;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Alex.C.P
 *
 * 
 */
public class Threadtest {

    /**
     * @param args
     */
    private Compare<Command<CTask>> compare = null;
    private PriorityQueue<Command<CTask>> REQUESTQUEUE = null;

    Threadtest() {
        compare = new Compare<Command<CTask>>();
        //compare =  new Compare< Command <CTask> >();
        //REQUESTQUEUE = new PriorityQueue<Command<CTask>>(10, compare);
        REQUESTQUEUE = new PriorityQueue(10, compare);

    }

    public void addtoQue(Command<CTask> e) {

        REQUESTQUEUE.add(e);

    }

    private void printQue() {

        for (Iterator iterator = REQUESTQUEUE.iterator(); iterator.hasNext();) {
            Command<CTask> type = (Command<CTask>) iterator.next();
            System.out.println("Type =" + type.m_key + " " + type.m_value);

        }

    }

    private void printHead() {
        System.out.println("Head of the Quue " + REQUESTQUEUE.peek().m_value);

    }

    static void giveMeAString(Object y) {
        y = "This is a string";
    }

    public static void main(String[] args) {

        Threadtest tst = new Threadtest();
        try {
            tst.run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void run() throws Exception {

        long starttime = 0;
        long endtime = 0;

        //For testing Custom ThreadPool
       /* ThreadPool<CTask> tpool = new ThreadPool<CTask>(2, 100, 1000);
        starttime = System.currentTimeMillis();

        for (int i = 1; i < 1000; ++i) {
        CTask task3 = new CTask();
        CTask task4 = new CTask();
        Command<CTask> cmd1 = new Command<CTask>(task3, "DoSomethig", 1, "key", 1, 2);
        tpool.QueueRequest(cmd1);
        Command<CTask> cmd4 = new Command<CTask>(task4, "DoSomethig", 1, "key2", 2, 5);
        //Command<CTask> cmd4 = new Command<CTask>(task4, "DoMultiplication", 1, "key2", 2, 5);
        tpool.QueueRequest(cmd4);
        }
        endtime = System.currentTimeMillis();

        System.out.println("Total Time Taken Loop 1= " + (endtime - starttime));
        Thread.sleep(5000);// Wait for a few seconds to take task statistics
        System.out.println("Total Tasks Initiated in CustomThreadPool = " + CTask.getInitiatedTasks());
        System.out.println("Total Tasks Completed in CustomThreadPool = " + CTask.getProcessedTasks());

       */
      /*

        for (int i = 1; i < 1000; ++i) {
        CTask task3 = new CTask();
        CTask task4 = new CTask();
        Command<CTask> cmd1 = new Command<CTask>(task3, "DoSomethig", 1, "key", 1, 2);
        tpool.QueueRequest(cmd1);
        //Thread.sleep(rm.nextInt());
        //Thread.sleep(100);
        Command<CTask> cmd4 = new Command<CTask>(task4, "DoMultiplication", 1, "key2", 2, 5);
        tpool.QueueRequest(cmd4);
        }
        endtime = System.currentTimeMillis();

        System.out.println("Total Time Taken loop 2= " + (endtime - starttime));
          Thread.sleep(5000);// Wait for a few seconds to take task statistics
        System.out.println("Total Tasks Initiated in CustomThreadPool = " + CTask.getInitiatedTasks());
        System.out.println("Total Tasks Completed in CustomThreadPool = " + CTask.getProcessedTasks());
*/
        //---------------------Using Java ThreadPool

  
       //ExecutorService pool = Executors.newCachedThreadPool();
       //ExecutorService pool = Executors.newFixedThreadPool(100);
      //  ExecutorService pool = new ThreadPoolExecutor(0, 100, 250, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
       //    new ThreadPoolExecutor.CallerRunsPolicy());//Dont use this as the calling thread may hang
        ExecutorService pool = new ThreadPoolExecutor(10, 500, 250, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());//
        starttime = System.currentTimeMillis();
        Thread.sleep(1000);
        for (int i = 1; i < 1000; ++i) {
            CTask task3 = new CTask();
            CTask task4 = new CTask();
            try {
                pool.execute(task3);
                pool.execute(task4);
            } catch (RejectedExecutionException e) {
                System.out.println("Task Rejected" + e.getMessage());
            }

            
        }

        endtime = System.currentTimeMillis();

        System.out.println("Total Time Taken For ThreadPool= " + (endtime - starttime));
        Thread.sleep(5000);
        System.out.println("Total Tasks Initiated in Java ThreadPool = " + CTask.getInitiatedTasks());
        System.out.println("Total Tasks Completed in Java ThreadPool = " + CTask.getProcessedTasks());

   
        //----------------End Using threadpool

        try {
            Thread.sleep(1000 * 60 * 3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
