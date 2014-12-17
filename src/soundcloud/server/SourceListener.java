/**
 * An Object from this class will count as a server socket for message from
 * the event source.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.concurrent.PriorityBlockingQueue;

public class SourceListener implements Runnable {

	private PriorityBlockingQueue<Message> queue;

	public SourceListener(PriorityBlockingQueue<Message> queue){
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
