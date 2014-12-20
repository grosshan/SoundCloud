/**
 * A class for storing messages in a queue. It has the same functionality like a priority queue, except that
 * the poll() operating blocks until an element with an expected sequence number occur.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.PriorityQueue;

public class MessageQueue extends PriorityQueue<Message>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3407330088669891183L; // eclipse wants that
	private int readCounter;
	private int writeCounter;
	
	public MessageQueue(){
		writeCounter = 1;
		readCounter = 1;
	}

	// private class to search for a method without creating a complex method object (including parsing etc.)
	private class MyInteger{
		
		public int i;
		public MyInteger(int i){
			this.i = i;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof Message) return ((Message)o).getNumber() == this.i;
			if(o instanceof Integer) return ((Integer)o).intValue() == this.i;
			return false;
		}
	}
	/**
	 * An overwritten method for adding an element. The counter of the next smallest element will be increased,
	 * and a threads that wait for an element to poll is notified when this element was added 
	 * 
	 * @param m Message that should be added to the queue
	 * @return true if and only if message was added
	 */	
	@Override
	public boolean add(Message m){
		return offer(m);
	}
	
	/**
	 * An overwritten method for adding an element. The counter of the next smallest element will be increased,
	 * and a threads that wait for an element to poll is notified when this element was added 
	 * 
	 * @param m Message that should be added to the queue
	 * @return true if and only if message was added
	 */	
	@Override
	public boolean offer(Message m){
		boolean ret_val;
		if (m.getNumber() == writeCounter){
			synchronized(this){
				ret_val = super.offer(m);
				// determine new writecounter
				while(this.contains(new MyInteger(writeCounter)))
					writeCounter++;
				if(writeCounter > readCounter);
					this.notifyAll();
			}
		} else {
			ret_val = super.offer(m);
		}
		return ret_val;		
	}
	
	/**
	 * An overwritten method for polling an element. Waits until the next expected message is available.
	 * @return the message when it becomes available, returns null if this operation was interrupted
	 */	
	@Override
	public Message poll(){
		if (writeCounter <= readCounter) {
			synchronized(this){
				while (writeCounter <= readCounter){
					try {
						this.wait();
					} catch (InterruptedException e) {
						return null;
					}
				}
			}
		}
		readCounter++;
		return super.poll();
	}
}
