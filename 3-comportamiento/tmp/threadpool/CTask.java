//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CTask implements Runnable {

    static final long starttime = System.currentTimeMillis();
    static AtomicInteger taskInitiated = new AtomicInteger(0);
    static AtomicInteger taskProcessed = new AtomicInteger(0);
    static Random rand = new Random();

    public int DoSomethig(int a, int b) {
        System.out.println("Inside DoSomething");
        int i=taskInitiated.incrementAndGet();
        int result = a + b;
        double tan = Math.tan(Math.atan(Math.tan(77666660)));
         i = rand.nextInt(5); 
      //if ((i == 22)||(i == 32)|| (i == 42) ||(i == 88) || (i == 5)) //To make hanging tasks consistent
         if(i==2)
         {
            System.out.println("Sleeping in " + Thread.currentThread().getName());
            try {
                Thread.sleep(60000*60);
            } catch (InterruptedException ex) {
                //Logger.getLogger(CTask.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
      else    {
      taskProcessed.incrementAndGet();}//simulate hang

        /*if (i == 4) {
            System.out.println("Sleeping in " + Thread.currentThread().getName());
            try {
                Thread.sleep(rand.nextInt(5) * 1000);
            } catch (InterruptedException ex) {
                //Logger.getLogger(CTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//simulate hang
         *
         */

        System.out.printf("The result is %d\n", result);

        
        return result;

    }

     public static int getProcessedTasks(){
        return taskProcessed.get();
    }
      public static int getInitiatedTasks(){
        return taskInitiated.get();
    }

    public int DoMultiplication(int a, int b) {
        System.out.println("Inside DoMultiplication");
        int result = a * b;
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            //  Logger.getLogger(CTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("The result is %d\n", result);
        return result;
    }

  

    @Override
    public void run() {
        DoSomethig(1, 2);

        return;

    }
}
