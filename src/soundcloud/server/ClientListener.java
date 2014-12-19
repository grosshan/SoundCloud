/**
 * An Object from this class will count as a server socket for message from
 * the user clients.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */

package soundcloud.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ClientListener implements Runnable {

	private UserRegistry registry;
	private ServerSocket servSocket;
	private int port;
	
	/**
	 * Creates a Listener. Listener opens server socket at given port.
	 * 
	 * @param registry Registry, where users will be registered.
	 * @param port Listener will accept user requests on this given port.
	 */
	public ClientListener(UserRegistry registry, int port){
		
	}
	
	/**
	 * Returns the port, the listener listens to.
	 * @return port 
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Runs this thread. Listener accepts user clients and register them in user registry. 
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
