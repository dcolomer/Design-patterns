package evolution.threads.threadmanger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class checks if a #ManagedThread is hung
 * If so based on the action specified when the thread is added to be managed
 * it can either restart the thread and add it again to be managed 
 * @author Alex.C.P
 *
 */
public class ThreadManager extends Thread{

	public static final int RESTART_THREAD =1;
	public static final int STOP_APP =2;

	private boolean isRunning= true;
	private final Collection<ManagedThreadData> managedThreads;
	private  long monitortime = 60*1000;



	public ThreadManager() {
		managedThreads = new HashSet<ManagedThreadData>();
		
	}

	/**
	 * This is the method to which a managed thread has to be added in case it has to be 
	 * monitored
	 * @param managedThread - The thread that has to be monitored
	 * @param managedAction - In case of hang the recovery action to be specified either RESTART_THREAD 
	 *  or STOP_APP . The STOP_APP stops the entire application , not implemented now
	 * @param threadchecktime - Reserved for future use - The periodic interval at which this thread has to be monitored
	 */
	public void manage(ManagedThread managedThread,int managedAction, long threadchecktime)
	{
		ManagedThreadData thrddata = new ManagedThreadData(managedThread,managedAction,threadchecktime);
		managedThreads.add(thrddata);
	}

	public void run()
	{
		while(isRunning)
		{
			for (Iterator<ManagedThreadData> iterator = managedThreads.iterator(); iterator.hasNext();) {
				ManagedThreadData thrddata = (ManagedThreadData) iterator.next();

				if(thrddata.getManagedThread().isHung())
				{
					
					switch (thrddata.getManagedAction()) 
					{
					case RESTART_THREAD:
						//remove from the manager
						iterator.remove();
						System.out.println("Going to restart the thread" );
						//stop the processing of this thread if possible
						thrddata.getManagedThread().stopProcessing();
						
						//This is fr unit tests
						if(thrddata.getManagedThread().getClass() == ThreadHangTester.class)
						{
							ThreadHangTester newThread =new ThreadHangTester("restarted_ThrdHangTest",5000,true);
							newThread.start();
							//add it back to be managed
							manage(newThread, thrddata.getManagedAction(), thrddata.getThreadChecktime());

						}
						break;

					default:
						System.out.println("No action specified- ignoring" );
						break;
					}//end switch
				}//end if hung

			}//end for
			try {
				Thread.sleep(monitortime);
			} catch (InterruptedException e) {
				System.out.println("Thread Manager interupted, shutting down");
				
			}
		}//end while
		
	}
	public void stopProcessing() {
		isRunning = false;

		//Force the thread to return from this method,
		// the thread is interrupted.
		this.interrupt();

	}
	/**
	 * A holder class to hold the thread information
	  *
	 */
	public static class ManagedThreadData{
		private ManagedThread managedThread;
		private int managedAction;
		private long threadChecktime;

		public ManagedThread getManagedThread() {
			return managedThread;
		}
		public int getManagedAction() {
			return managedAction;
		}
		public long getThreadChecktime() {
			return threadChecktime;
		}
		public ManagedThreadData(ManagedThread managedThread, int managedAction,
				long threadChecktime) {
			super();
			this.managedThread = managedThread;
			this.managedAction = managedAction;
			this.threadChecktime = threadChecktime;
		}

	}
	
	

	/**
	 * This method sets the time for the threadmanager to periodically moniotr
	 * the managed threads
	 * @param monitortime - time in milliseconds
	 */
	public void setMonitortime(long monitortime) {
		this.monitortime = monitortime;
	}

	/**
	 * Removes all threads from being monitored by this thread
	 */
	public void removeallthreads() {
		managedThreads.clear();
	};
	
	/**
	 * For unit test
	 * @return
	 */
	public int getManagedThreadCount()
	{
		return managedThreads.size();
	}
	
	/**
	 * For unit test
	 * @return
	 */
	public String[] getManagedThreadNames()
	{
		String name[] = new String[managedThreads.size()];
		int i =-1;
		for (Iterator iterator = managedThreads.iterator(); iterator.hasNext();) {
			ManagedThreadData type = (ManagedThreadData) iterator.next();
			name[++i]=type.getManagedThread().getName();
		}
		return name;
	}
}
