/**
 * An Object from this class will count as a server socket for message from
 * the user clients.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */

package soundcloud.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener extends Thread {

	private UserRegistry registry;
	private ServerSocket servSocket;
	private int port;
	
	/**
	 * Creates a Listener. Listener opens server socket at given port.
	 * 
	 * @param registry Registry, where users will be registered.
	 * @param port Listener will accept user requests on this given port.
	 */
	public ClientListener(UserRegistry registry, int port) throws IOException{
		this.registry = registry;
		servSocket = new ServerSocket(port);
		this.port = servSocket.getLocalPort();
		
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
		while(!this.isInterrupted()){
			try{
				Socket socket = servSocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				int id = Integer.parseInt(reader.readLine());

				registry.registerUser(id);
				User u = registry.getUser(id);
				
				u.closeConnection();
				u.openConnection(socket);
				
			} catch (IOException e){
				e.printStackTrace();
				if(servSocket.isClosed() || !servSocket.isBound())
					this.interrupt();
			}
			
		}

	}

}
