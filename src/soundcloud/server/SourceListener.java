/**
 * An Object from this class will count as a server socket for message from
 * the event source.
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

public class SourceListener extends Thread{

	private MessageQueue queue;
	private ServerSocket servSocket;
	private Socket socket;
	private int port;

	/**
	 * Constructor. Will create a source listener with respect to the given queue and the given port.
	 * @param queue queue, where messages should be stored.
	 * @param port port, where the listener wants to listen to.
	 * @throws IOException 
	 */
	public SourceListener(MessageQueue queue, int port) throws IOException{
		this.queue = queue;
		this.servSocket = new ServerSocket(port);
		this.port = this.servSocket.getLocalPort();
	}

	/**
	 * Returns the port, the listener listens to.
	 * @return port 
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Method to start the Executor. The Listener will read & parse an incoming message and store it into the
	 * queue.
	 */
	@Override
	public void run() {
		// open Socket
		try {
			char[] buffer = new char[256];
			StringBuffer message = new StringBuffer(64);

			this.socket = servSocket.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));

			while(!isInterrupted()){
				// read some chars
				int numChars = reader.read(buffer, 0, buffer.length);
				
				// now extract messages
				
				for(int i = 0; i < numChars; i++){
					message.append(buffer[i]);
					
					if(buffer[i] == '\n'){
						queue.add(new Message(message.toString()));
						message = new StringBuffer(64);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
