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
	private ArrayList<MyExecutor> executors;
	
	/**
	 * A private class that represents a worker. Messages will be parsed and
	 * sent - ordered and in parallel - to user clients.
	 * 
	 * @author Michael Grosshans
	 * @version 1.0
	 */	
	private class MyExecutor extends Thread{

		private MessageCollector collector;
		private int id;
	
		/**
		 * Constructor that creates this runnable object and register invoking MessageCollector
		 * @param collector the parent object
		 * @param id the id of this thread (corresponds to the pipe)
		 */
		public MyExecutor(MessageCollector collector, int id){
			this.collector = collector;
			this.id = id;
		}
		
		/**
		 * Start this runnable object. It will run until it is interrupted. 
		 */
		@Override
		public void run() {
			while(!this.isInterrupted()){
				// lock queue & collect message
				Message m = this.collector.queue.poll(id);
				
				if (m == null) { // interrupted poll -> interrupt/close this thread
					this.interrupt();
				}
				else { 
					// we have a message
					
					// determine recipients
					Collection<User> recipients = null;
					switch(m.getType()){
					case Broadcast: // rec: all users
						recipients = registry.getAllUser(id);
						break;
					case Status: // rec: followers
						recipients = m.getSource().getFollowers(id);
						break;
					case Unfollow: // rec: none
						recipients = new ArrayList<User>();
						break;
					case Follow: // rec: maybe
						recipients = new ArrayList<User>();
						int t_pipe = m.getTarget().getID() % collector.executors.size(); 
						if(t_pipe == id)
							recipients.add(m.getTarget());
						break;
					case Private: // rec: target
						recipients = new ArrayList<User>();
						recipients.add(m.getTarget());
						break;
					}
					
					// determine follow/unfollow operation on users
					int s_pipe;
					switch(m.getType()){
					case Unfollow:
						s_pipe = m.getSource().getID() % collector.executors.size();
						if(s_pipe == id)
							m.getTarget().removeFollower(m.getSource());
						break;
					case Follow:
						s_pipe = m.getSource().getID() % collector.executors.size();
						if(s_pipe == id)
							m.getTarget().addFollower(m.getSource());
						break;
					default:
						break;
					}
					
					// send messages & unlock recipients
					for(User u : recipients){
						u.sendMessage(m);
					}
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
		new ReentrantLock();
		this.executors = new ArrayList<MyExecutor>();
		for(int i = 0; i < numThreads; i++){
			this.executors.add(new MyExecutor(this, i));
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
