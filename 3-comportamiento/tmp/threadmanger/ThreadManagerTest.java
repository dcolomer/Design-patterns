package evolution.threads.threadmanger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class to test #ThreadManager
 * @author Alex.C.P
 *
 */
public class ThreadManagerTest {
	
	ThreadManager thrdManger = null;

	@Before
	public  void start()
	{
		BasicConfigurator.configure();
		thrdManger = new ThreadManager(null);
	}
	
	@After
	public void cleanup()
	{
		thrdManger.removeallthreads();
	}
	@Test
	//Testing a non hanging thread
	public void testmanageNotHanging()
	{
		//for this we need to create a new thread class
		ThreadHangTester testthread = new ThreadHangTester("threadhangertest",2000,false);
		testthread.start();
		thrdManger.manage(testthread, ThreadManager.RESTART_THREAD, 10);
		thrdManger.start();
		try {
			Thread.sleep(3000);//sleep 
		} catch (InterruptedException e) {
				//do nothing here - unit test code
		}
		Assert.assertTrue(testthread.isAlive());
	}
	
	@Test
	//Testing a hanging thread
	public void testmanageHanging()
	{
		//for this we need to create a new thread class
		ThreadHangTester testthread = new ThreadHangTester("threadhangertest",2000,true);
		testthread.start();
		thrdManger.manage(testthread, ThreadManager.RESTART_THREAD, 0);
		thrdManger.setMonitortime(500);//monitor every second
		thrdManger.start();
		try {
			Thread.sleep(5000);//
		} catch (InterruptedException e) {
			//do nothing here - unit test code
		}
		Assert.assertFalse(testthread.isAlive());
	}
	
	//Tets to see if threadmanager is restarting the thread
	@Test
	public void checkthreadrestart()
	{
		//for this we need to create a new thread class
		ThreadHangTester testthread = new ThreadHangTester("threadhangertest",2000,true);
		testthread.start();
		thrdManger.manage(testthread, ThreadManager.RESTART_THREAD, 0);
		thrdManger.setMonitortime(500);//monitor every second
		thrdManger.start();
		try {
			Thread.sleep(3000);//
		} catch (InterruptedException e) {
			//do nothing here - unit test code
		}
		
		Assert.assertTrue(thrdManger.getManagedThreadCount() ==1);
		String[] thrdname = thrdManger.getManagedThreadNames();
		Assert.assertTrue(thrdname[0].contains("restart"));
		
		
	}
	
	@Test
	public void thrManagerShutdown()
	{
		//for this we need to create a new thread class
		ThreadHangTester testthread = new ThreadHangTester("threadhangertest",2000,false);
		testthread.start();
		thrdManger.manage(testthread, ThreadManager.RESTART_THREAD, 10);
		thrdManger.setMonitortime(1000);//monitor every second
		thrdManger.start();
		try {
			Thread.sleep(3000);//sleep 10 seconds
			thrdManger.stopProcessing();
			Thread.sleep(200);//sleep 2 sec for thrdmanager to shut down
		} catch (InterruptedException e) {}
		
		Assert.assertFalse(thrdManger.isAlive());
		
	}
}
