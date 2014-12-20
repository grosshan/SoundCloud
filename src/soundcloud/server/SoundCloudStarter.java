/**
 * This is the class that will build up the whole bunch of threads and object that are need
 * to solve the back-end challenge from SoundCloud
 */
package soundcloud.server;

import java.io.IOException;

public class SoundCloudStarter {

	/**
	 * Just the main method
	 * @param args command-line arguments are ignored at this point.
	 */
	public static void main(String[] args) {
		int numThreads = 4;
		int sourcePort = 9090;
		int userPort = 9099;

		try {
			
			UserRegistry registry = new UserRegistry();
			MessageQueue queue = new MessageQueue();
			
			MessageCollector collector = new MessageCollector(queue, registry, numThreads);
			ClientListener userListen = new ClientListener(registry, userPort);
			SourceListener sourceListen = new SourceListener(queue, sourcePort);
			
			collector.start();
			userListen.start();
			sourceListen.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
