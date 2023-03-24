package evolution.threads.threadmanger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * All classes which needs thread monitoring must extend from this class
 * The #ThreadManager class can do the monitoring of all classes that extends
 * from this class. The inherited class should correctly set the lastprocessingtime
 * to the current time before starting a task.
 * @author  @author Alex.C.P
 *
 */
public abstract class ManagedThread extends Thread {

	protected  final AtomicLong lastprocessingtime ;
	protected  long maxprocessingtime=0;
	private String threadname;

	
		public ManagedThread(String name,long maxprocessing) {
		super(name);
		threadname = name;
		maxprocessingtime=maxprocessing;
		lastprocessingtime = new AtomicLong(0);
	}

	abstract public void stopProcessing();
	

	public final boolean isHung() {

		System.out.println("Checking if thread is hung" +threadname);
		if(lastprocessingtime.get()==0)
		{
			System.out.println("Thread is NOT hung or lastprocessingtime is not set/reset");
			return false;
		}

		if(maxprocessingtime==0)
		{
			System.out.println("Thread is NOT monitored properly");
			return false;
		}

		if(System.currentTimeMillis() - lastprocessingtime.get() > maxprocessingtime )
		{
			System.out.println("Thread is hung");
			return true;
		}

		System.out.println("Thread is NOT hung ");
		return false;

	}
}
