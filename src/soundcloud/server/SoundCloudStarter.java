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
		int numThreadsSource = 2;
		int numThreadsClient = 2;
		int sourcePort = 9090;
		int userPort = 9099;

		try {
			
			UserRegistry registry = new UserRegistry(numThreadsClient);
			MessageQueue queue = new MessageQueue(numThreadsSource, numThreadsClient);
			
			MessageCollector collector = new MessageCollector(queue, registry, numThreadsClient);
			ClientListener userListen = new ClientListener(registry, userPort);
			SourceListener sourceListen = new SourceListener(queue, registry, sourcePort, numThreadsSource);
			
			collector.start();
			userListen.start();
			sourceListen.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
