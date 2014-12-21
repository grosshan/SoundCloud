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
import java.util.ArrayList;

public class SourceListener{

	private MessageQueue queue;
	private UserRegistry registry;
	private ServerSocket servSocket;
	private Socket socket;
	private BufferedReader reader;
	private ArrayList<MyListener> listeners;
	private int port;

	private class MyListener extends Thread{
		
		private int id;
		private SourceListener source;
		
		
		public MyListener(SourceListener source, int id){
			this.source = source;
			this.id = id;
		}
		
		@Override 
		public void run(){
			try{
				String message = null;
				while(!isInterrupted()){
					synchronized(source.reader){
						message = source.reader.readLine();
					}
					if(message == null)
						this.interrupt();
					else {
						message += "\r\n";
						this.source.queue.offer(new Message(message, registry), id);
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
				this.interrupt();
			}
			
		}
	}
	/**
	 * Constructor. Will create a source listener with respect to the given queue and the given port.
	 * @param queue queue, where messages should be stored.
	 * @param port port, where the listener wants to listen to.
	 * @throws IOException 
	 */
	public SourceListener(MessageQueue queue, UserRegistry registry, int port, int numPipes) throws IOException{
		this.queue = queue;
		this.registry = registry;
		this.servSocket = new ServerSocket(port);
		this.port = this.servSocket.getLocalPort();
		this.listeners = new ArrayList<MyListener>(numPipes);
		for(int i = 0; i < numPipes; i++){
			this.listeners.add(new MyListener(this,i));
		}
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
	public void start() {
		try {

			this.socket = servSocket.accept();
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			for(int i = 0; i < this.listeners.size(); i++){
				this.listeners.get(i).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
