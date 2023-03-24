package evolution.threads.threadmanger;



public class ThreadHangTester extends ManagedThread {

	private boolean ishung;
	private boolean isAlive;
	
	/*static {
		LogFactory.setLogFactory(new Log4jLogFactory());
	}
	private final LogAdapter logger = LogFactory.getLogger(ThreadHangTester.class);
         *
         */
	
	public ThreadHangTester(String string,long maxwaitTime,boolean simulatehang) {
		super(string,maxwaitTime);
		isAlive = true;
		ishung=simulatehang;
	}

	
	@Override
	public void stopProcessing() {
		isAlive = false;
		this.interrupt();

	}
	public void run()
	{
		while(isAlive)
		{
			//logger.debug("Going to run");
			
			lastprocessingtime.set(System.currentTimeMillis());
			
			if(ishung)
			{
				try {
					//logger.debug("Going to do sleep for 10 mts");
					Thread.sleep(1000*60*10);
					//logger.debug("Out of sleep");
				} catch (InterruptedException e) {
					;//will be interupted by the unit test					
				}
			}
			
			//do some processor intesnsive task here
			//logger.debug("Going to do processing of CPU intensive task");
			double tanof =  122222222233434343242342342342131232132.12;
			Math.tan(tanof);
			//logger.debug("Out of CPU processing");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				lastprocessingtime.set(0);
			}
			
		}
		
	}
	
	


}
