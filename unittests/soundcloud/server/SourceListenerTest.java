package soundcloud.server;

import static org.junit.Assert.*;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.junit.Test;

public class SourceListenerTest {

	@Test
	public void connectTest() {
		
		try {
			MessageQueue queue = new MessageQueue();
			SourceListener listen = new SourceListener(queue, 0);
			Thread t = new Thread(listen);
			t.start();
			
			Socket socket = new Socket("localhost", listen.getPort());
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			out.print("2|B\r\n");
			out.print("4|B\r\n");
			out.print("1|B\r\n");
			out.print("3|B\r\n");
			out.flush();
			
			Thread.sleep(1000);
			assertTrue(queue.size() == 4);
			assertTrue(queue.poll().getNumber() == 1);
			assertTrue(queue.poll().getNumber() == 2);
			assertTrue(queue.poll().getNumber() == 3);
			assertTrue(queue.poll().getNumber() == 4);
			
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
	}

}
