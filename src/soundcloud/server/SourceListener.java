/**
 * An Object from this class will count as a server socket for message from
 * the event source.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SourceListener implements Runnable{

	private MessageQueue queue;
	private ServerSocket servSocket;
	private Socket socket;
	private int port;

	/**
	 * Constructor. Will create a source listener with respect to the given queue and the given port.
	 * @param queue queue, where messages should be stored.
	 * @param port port, where the listener wants to listen to.
	 */
	public SourceListener(MessageQueue queue, int port){
		
	}

	/**
	 * Returns the port, the listener listens to.
	 * @return port 
	 */
	public int getPort() {
		return port;
	}
	
	@Override
	/**
	 * Method to start the Executor. The Listener will read & parse an incoming message and store it into the
	 * queue.
	 */
	public void run() {
		
	}

}
