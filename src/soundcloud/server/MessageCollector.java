/**
 * A class for collecting messages from a queue. Messages will be parsed and
 * sent - ordered and in parallel - to user clients.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;


public class MessageCollector{

	private MessageQueue queue;
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
	private class MyExecutor extends Thread{

		private UserRegistry registry;
		private ReentrantLock queueLock;
		private MessageQueue queue;
	
		/**
		 * Constructor that creates this runnable object and register the shared lock, queue and
		 * registry object.
		 * @param registry the shared registry
		 * @param queueLock the shared lock
		 * @param queue the shared queue
		 */
		public MyExecutor(UserRegistry registry,
				ReentrantLock queueLock, MessageQueue queue){
			this.registry = registry;
			this.queueLock = queueLock;
			this.queue = queue;
		}
		
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
		@Override
		public void run() {
			while(!this.isInterrupted()){
				// lock queue & collect message
				this.queueLock.lock();
				Message m = this.queue.poll();
				
				if (m == null) { // interrupted poll -> unlock queue and interrupt/close this thread
					this.interrupt();
					this.queueLock.unlock();
				}
				else { 
					// we have a message
					
					// determine recipients
					Collection<User> recipients;
					switch(m.getType()){
					case Broadcast: // rec: all users
						recipients = registry.getAllUser();
						break;
					case Status: // rec: followers
						recipients = m.getSource().getFollowers();
						break;
					case Unfollow: // rec: none
						recipients = new ArrayList<User>();
						break;
					default: // rec: target
						recipients = new ArrayList<User>();
						recipients.add(m.getTarget());
						break;
					}
					
					// determine follow/unfollow operation on users
					switch(m.getType()){
					case Unfollow:
						m.getTarget().removeFollower(m.getSource());
						break;
					case Follow:
						m.getTarget().addFollower(m.getSource());
						break;
					default:
						break;
					}
					
					// lock all recipients
					for(User u : recipients){
						u.lock();
					}
					
					
					// send messages & unlock recipients
					for(User u : recipients){
						u.sendMessage(m);
						u.unlock();
					}
					// unlock queue
					this.queueLock.unlock();
				}
			}
		}
	
	}
	
	/**
	 * The constructor. Will register the queue and user registry for this thread.
	 * @param queue queue of input message
	 * @param registry the user registry
	 * @param numThreads number of threads the collector should use 
	 */
	public MessageCollector(MessageQueue queue,
			UserRegistry registry, int numThreads){
		this.queue = queue;
		this.registry = registry;
		this.queueLock = new ReentrantLock();
		this.executors = new ArrayList<MyExecutor>();
		for(int i = 0; i < numThreads; i++){
			this.executors.add(new MyExecutor(this.registry, this.queueLock, this.queue));
		}
	}
	
	/**
	 * Start the working process. e.g. all threads will be created and started
	 * <NOT THREAD SAFE>
	 */
	public void start(){
		for(int i = 0; i < this.executors.size(); i++){
			this.executors.get(i).start();
		}
	}
	
	/**
	 * End all working processes.
	 * <NOT THREAD SAFE>
	 */
	public void stop(){
		for(int i = 0; i < this.executors.size(); i++){
			this.executors.get(i).interrupt();;
		}		
	}
}
