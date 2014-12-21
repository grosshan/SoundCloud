package soundcloud.server;

import static org.junit.Assert.*;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.junit.Test;

public class SourceListenerTest {

	@Test (timeout = 2000)
	public void connectTest() {
		
		try {
			// build up pipes & registry
			int sourcePipes = 2;
			int clientPipes = 3;
			
			MessageQueue queue = new MessageQueue(sourcePipes, clientPipes);
			UserRegistry registry = new UserRegistry(clientPipes);
			SourceListener listen = new SourceListener(queue, registry, 0, sourcePipes);
			
			// build output stream & connect them
			Socket socket = new Socket("localhost", listen.getPort());
			listen.start();
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// send messages to listener
			out.print("12|S|1\r\n");
			out.print("11|F|3|4\r\n");
			out.print("2|F|2|3\r\n");
			out.print("3|B\r\n");
			out.print("8|S|6\r\n");
			out.print("1|F|1|4\r\n");
			out.print("4|F|3|2\r\n");
			out.print("5|F|5|4\r\n");
			out.print("7|P|1|2\r\n");
			out.print("6|F|4|1\r\n");
			out.print("9|B\r\n");
			out.print("10|F|5|3\r\n");
			out.flush();
			Thread.sleep(100);
			
			// check if messages are here
			assertTrue(queue.inputSize() == 0);
			assertTrue(queue.outputSize() == 20);
			
			// check all queues and elements seperately
			assertTrue(queue.poll(0).getNumber() == 2);
			assertTrue(queue.poll(0).getNumber() == 3);
			assertTrue(queue.poll(0).getNumber() == 8);
			assertTrue(queue.poll(0).getNumber() == 9);
			assertTrue(queue.poll(0).getNumber() == 10);
			assertTrue(queue.poll(0).getNumber() == 12);

			assertTrue(queue.poll(1).getNumber() == 1);
			assertTrue(queue.poll(1).getNumber() == 3);
			assertTrue(queue.poll(1).getNumber() == 5);
			assertTrue(queue.poll(1).getNumber() == 6);
			assertTrue(queue.poll(1).getNumber() == 8);
			assertTrue(queue.poll(1).getNumber() == 9);
			assertTrue(queue.poll(1).getNumber() == 11);
			assertTrue(queue.poll(1).getNumber() == 12);

			assertTrue(queue.poll(2).getNumber() == 3);
			assertTrue(queue.poll(2).getNumber() == 4);
			assertTrue(queue.poll(2).getNumber() == 7);
			assertTrue(queue.poll(2).getNumber() == 8);
			assertTrue(queue.poll(2).getNumber() == 9);
			assertTrue(queue.poll(2).getNumber() == 12);

			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
	}

}
