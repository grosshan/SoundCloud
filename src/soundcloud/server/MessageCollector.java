/**
 * A class for collecting messages from a queue. Messages will be parsed and
 * sent - ordered and in parallel - to user clients.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;


public class MessageCollector{

	private PriorityBlockingQueue<Message> queue;
	private UserRegistry registry;
	private ReentrantLock queueLock;
	private ArrayList<MyExecutor> executors;
	
	/**
	 * A private class that represents a worker. Messages will be parsed and
	 * sent - ordered and in parallel - to user clients.
	 * 
	 * @author Michael Grosshans
	 * @version 1.0
	 */	
	private class MyExecutor implements Runnable{

		private UserRegistry registry;
		private ReentrantLock queueLock;
		private PriorityBlockingQueue<Message> queue;
	
		/**
		 * Constructor that creates this runnable object and register the shared lock, queue and
		 * registry object.
		 * @param registry the shared registry
		 * @param queueLock the shared lock
		 * @param queue the shared queue
		 */
		public MyExecutor(UserRegistry registry,
				ReentrantLock queueLock, PriorityBlockingQueue<Message> queue){
		}
		
		@Override
		/**
		 * Start this runnable object. It will run until it is interrupted. Protocol:
		 * 1. lock the queue
		 * 2. collect message
		 * 3. lock all recipients
		 * 4. unlock queue
		 * 5. for all recipients:
		 * 5.1. send message
		 * 5.2. send unlock recipient
		 */
		public void run() {
			// TODO Auto-generated method stub
		
		}
	
	}
	
	/**
	 * The constructor. Will register the queue and user registry for this thread.
	 * @param queue queue of input message
	 * @param registry the user registry
	 * @param numThreads number of threads the collector should use 
	 */
	public MessageCollector(PriorityBlockingQueue<Message> queue,
			UserRegistry registry, int numThreads){
		
	}
	
	/**
	 * Start the working process. e.g. all threads will be created and started
	 */
	public void start(){
		
	}
	
	/**
	 * End all working processes.
	 */
	public void stop(){
		
	}
}
