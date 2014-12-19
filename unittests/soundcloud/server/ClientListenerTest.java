package soundcloud.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.junit.Test;

public class ClientListenerTest {

	@Test
	public void registerUser() {
		
		// set up a Listener
		UserRegistry registry = new UserRegistry();
		ClientListener listen = new ClientListener(registry, 0);
		Thread t = new Thread(listen);
		t.start();
		
		try {
			Socket socket1 = new Socket("localhost",listen.getPort());
			Socket socket2 = new Socket("localhost",listen.getPort());

			PrintWriter out1 = new PrintWriter(new OutputStreamWriter( socket1.getOutputStream()));
			PrintWriter out2 = new PrintWriter(new OutputStreamWriter( socket2.getOutputStream()));
			
			BufferedReader in1 = new BufferedReader(new InputStreamReader( socket1.getInputStream()));
			BufferedReader in2 = new BufferedReader(new InputStreamReader( socket2.getInputStream()));

			assertTrue(registry.getAllUser().size() == 0);

			out1.print("292\r\n");
			assertTrue(registry.hasUser(292));
			assertFalse(registry.hasUser(13));
			assertFalse(registry.hasUser(15));
			assertTrue(registry.getAllUser().size() == 1);

			out2.print("13\r\n");
			assertTrue(registry.hasUser(292));
			assertTrue(registry.hasUser(13));
			assertFalse(registry.hasUser(15));
			assertNull(registry.getUser(15));
			assertTrue(registry.getAllUser().size() == 2);

			User first = registry.getUser(292);
			first.sendMessage(new Message("66|F|60|50"));
			User second = registry.getUser(13);
			second.sendMessage(new Message("1|U|12|9"));
			
			assertTrue(in1.readLine().equals("666|F|60|50"));
			assertTrue(in2.readLine().equals("1|U|12|9"));
			socket1.close();
			socket2.close();
			
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}

}
