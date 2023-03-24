package evolution.threads.threadmanger;

import evolution.threads.threadpool.CTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * ENUM based Singleton Pattern 
 * from Josh Block's Effective Java. Valid from Java 1.5 onwards
  * **/
public enum TaskQueue {
	INSTANCE;
	
	private final BlockingQueue<CTask> alarmnewlist;

	private static int trapRejectedfromOES=0;

	private static int trapReceivedfromOES=0;

	
	
	/** Constructor
	 * Intilizes the queue to one lakh entries
	 * **/
	private TaskQueue()
	{
        //putting capacity as one lakh; heap size here reaches around 50 MB without consumption
		alarmnewlist= new LinkedBlockingQueue<CTask>(100000);

		
	};
	
	/** This method is thread safe and can be used to put notifications in the queue.
	 * This is non blocking. In case the consumer thread does not cosume then the queue will
	 * accept to a maximum 1 lakh notificaiton. The heap size here is max at 50 MB
	 * Beyond which the function will return failure.
	 * @param SNMPNotificaion
	 * 				Notificaion of type SNMPNotificaion which needs to be send as traps
	 * @return 
	 * 	true in case insertion is successful; that is the queue has not reached max capacity 
	 *  false in case the insertion failed; that is queue has reached max capacity
	 * */
	public boolean putNotification(final CTask snmpnotificaion)
	{
		final boolean  suceess = alarmnewlist.offer(snmpnotificaion);
		if(!suceess)
		{
			//logger.error("Queue Capcity reached, rejecting notificaiotn");
                }
		
		return suceess;
		
	}
	/** This method is used for getting the notification from the queue by the consumer
	 *  THIS IS A BLOCKING CALL and should only be called from a thread (not in main thread)
	 *  This method waits till an entry is present in the queue.
	 *   @throws  InterruptedException
	 * */
	public CTask getNotificaion() throws InterruptedException
	{
		 return  alarmnewlist.take();
		
	}
	
	/**
	 * Clears all the alarms in the queue.
	 */
	public void clearAlarms(){
		alarmnewlist.clear();
	}
	
	public int getQueueSize(){
		return alarmnewlist.size();
	}
	
}
