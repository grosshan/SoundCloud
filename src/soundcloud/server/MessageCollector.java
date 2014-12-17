/**
 * A class for collecting messages from a queue. Messages will be parsed and
 * sent - ordered and in parallel - to user clients.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageCollector implements Runnable{

	private PriorityBlockingQueue<Message> queue;
	private ThreadPoolExecutor executors;
	private UserRegistry registry;
	
	public class MyExecutor implements Runnable{

		private Message[] messages;
		private UserRegistry registry;
		
		public MyExecutor(Message[] messages, 
				UserRegistry registry){
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	public MessageCollector(PriorityBlockingQueue<Message> queue,
			UserRegistry registry){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
