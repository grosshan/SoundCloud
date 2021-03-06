/**
 * A class for storing messages in a queue. It has the same functionality like a priority queue, except that
 * the poll() operating blocks until an element with an expected sequence number occur.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {

	private ArrayList<HashMap<Integer, Message>> inList;
	private ArrayList<LinkedBlockingQueue<Message>> outList;
	private HardWorkingProcess myChineseWorker;
	
	/**
	 * A private class that represents a worker. 	 
	 * Subsequently, this thread will bring messages from the input queue to the output queue.
	 * 
	 * @author Michael Grosshans
	 * @version 1.0
	 */	
	private class HardWorkingProcess extends Thread{
		
		private MessageQueue queue;
		private int writeCounter;
		
		public HardWorkingProcess(MessageQueue queue){
			this.queue = queue;
			writeCounter = 1;
		}
		
		@Override
		public void run(){
			while(!isInterrupted()){
				boolean found = false;
				Message m = null;
				int numTry = 0;
				
				// search the next message
				while (!found){
					synchronized(queue.inList.get(numTry % queue.inList.size())){
						m = queue.inList.get(numTry % queue.inList.size()).remove(writeCounter);
					}
					if(m != null)
						found = true;
					numTry++;
				}
				
				// put it to the output queues
				try{
					int s_pipe, t_pipe;
					
					switch (m.getType()){
					case Follow: // register in source & target pipe (but only once)
						s_pipe = m.getSource().getID() % queue.outList.size();
						t_pipe = m.getTarget().getID() % queue.outList.size();
						if(s_pipe != t_pipe){
							queue.outList.get(s_pipe).put(m);
							queue.outList.get(t_pipe).put(m);
						} else 
							queue.outList.get(s_pipe).put(m);
						break;
					case Unfollow: // Follow / Unfollow -> source too (synch with status update)
						s_pipe = m.getSource().getID() % queue.outList.size();
						queue.outList.get(s_pipe).put(m);
						break;
					case Private: // Follow / Unfollow / Private
						t_pipe = m.getTarget().getID() % queue.outList.size();
						queue.outList.get(t_pipe).put(m);
						break;
					case Broadcast:
					case Status:
						for(int i = 0; i < queue.outList.size(); i++){
							queue.outList.get(i).put(m);
						}
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					this.interrupt();
				}
				
				// prepare for next element
				writeCounter++;
			}
		}
	}
	
	/**
	 * Constructor
	 * @param sourcePipes number of pipes to read from
	 * @param clientPipes number of pipes to write to
	 */
	public MessageQueue(int sourcePipes, int clientPipes){
		inList = new ArrayList<HashMap<Integer, Message>>(sourcePipes);
		for(int i = 0; i < sourcePipes; i++){
			inList.add(new HashMap<Integer, Message>(1000));
		}
		outList = new ArrayList<LinkedBlockingQueue<Message>>(clientPipes);
		for(int i = 0; i < clientPipes; i++){
			outList.add(new LinkedBlockingQueue<Message>());
		}
		myChineseWorker = new HardWorkingProcess(this);
		myChineseWorker.start();
	}

	/**
	 * An method that returns the current size of input queue. 
	 * @return size of the queue
	 */	
	public int inputSize(){
		int size = 0;
		for(int i = 0; i < inList.size(); i++){
			synchronized(inList.get(i)){
				size += inList.get(i).size();
			}
		}
		return size;
	}

	/**
	 * An method that returns the current size of output queue. 
	 * @return size of the queue
	 */	
	public int outputSize(){
		int size = 0;
		for(int i = 0; i < outList.size(); i++){
			synchronized(outList.get(i)){
				size += outList.get(i).size();
			}
		}
		return size;
	}

	/**
	 * An method for adding an element. The counter of the next smallest element will be increased,
	 * and a threads that wait for an element to poll is notified when this element was added 
	 * @param m Message that should be added to the queue
	 * @param id the thread id that offers this message
	 */	
	public void offer(Message m, int id){
		if(m.getNumber() % 100000 == 0)
			System.out.println("Message Nr.: " + m.getNumber() + " Input-Buffer Size: " + this.inputSize()  + " Output-Buffer Size: " + this.outputSize());
		
		synchronized(inList.get(id)){
			inList.get(id).put(m.getNumber(), m);
		}
	}
	
	/**
	 * An method for polling an element for sender with specific id. Waits until the a message is available.
	 * Only one poll at a time is allowed! 
	 * @param id the id of the polling sender process
	 * @return the message when it becomes available, returns null if this operation was interrupted
	 */	
	public Message poll(int id){
		Message m;
		
		try {
			m = outList.get(id).take();
		} catch (InterruptedException e) {
			m = null;
		}
		return m;
	}
}
