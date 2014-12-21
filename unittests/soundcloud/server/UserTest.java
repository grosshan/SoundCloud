package soundcloud.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

public class UserTest {

	public class MyListener implements Runnable{

		ServerSocket serv_socket;
		Socket socket;
		int port;
		
		public MyListener() throws IOException{
			serv_socket = new ServerSocket(0);
			port = serv_socket.getLocalPort();
		}
		
		@Override
		public void run() {
			try {
				socket = serv_socket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Test
	public void followerTest(){
		User u1 = new User(1,2);
		User u2 = new User(2,2);
		User u3 = new User(2,2);
		User u4 = new User(3,2);
		
		assertNotNull(u1.getFollowers(0));
		assertNotNull(u1.getFollowers(1));
		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 0);
		
		u1.addFollower(u1);
		
		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 1);

		u1.addFollower(u2);
		u1.addFollower(u3);

		assertTrue(u1.getFollowers(0).size() == 1);
		assertTrue(u1.getFollowers(1).size() == 1);
		assertTrue(u1.getFollowers(0).get(0) == u2);
		assertTrue(u1.getFollowers(1).get(0) == u1);
		
		u1.removeFollower(u3);
		
		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 1);

		u1.removeFollower(u2);
		u1.addFollower(u4);

		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 2);

		u1.removeFollower(u1);

		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 1);
		
		u1.removeFollower(u4);

		assertTrue(u1.getFollowers(0).size() == 0);
		assertTrue(u1.getFollowers(1).size() == 0);
	}

	@Test( timeout = 2000 )
	public void messageTest() {
		
		MyListener u1_listen;
		try {
			u1_listen = new MyListener();
			new Thread(u1_listen).start();
			
			// build endpoint
			Socket u_end = new Socket("localhost",u1_listen.port);
			Thread.sleep(10);
			BufferedReader reader = new BufferedReader(new InputStreamReader(u1_listen.socket.getInputStream()));
			
			// set up registry
			User u1 = new User(1,1);
			UserRegistry registry = new UserRegistry(1);
			registry.registerUser(u1);
			
			// store messages
			String s1 = "1|F|1|1\r";
			String s2 = "2|F|1|1\r\n";
			String s3 = "3|F|1|1\n";
			u1.sendMessage(new Message(s1, registry));
			u1.sendMessage(new Message(s2, registry));
			u1.sendMessage(new Message(s3, registry));
			
			assertNotNull(u1.getMessages());
			assertTrue(u1.getMessages().size() == 3);
			assertTrue(u1.getMessages().peekFirst().getNumber() == 1);
			
			// open connectioen & flush messages
			u1.openConnection(u_end);
			assertNotNull(u1.getMessages());
			assertTrue(u1.getMessages().size() == 0);
			char[] ch1 = new char[s1.length()];
			char[] ch2 = new char[s2.length()];
			char[] ch3 = new char[s3.length()];
			reader.read(ch1, 0, s1.length());
			reader.read(ch2, 0, s2.length());
			reader.read(ch3, 0, s3.length());
			assertTrue((new String(ch1)).equals(s1));
			assertTrue((new String(ch2)).equals(s2));
			assertTrue((new String(ch3)).equals(s3));

			// send new message & flush
			u1.sendMessage(new Message(s1, registry));
			reader.read(ch1, 0, s1.length());
			assertTrue((new String(ch1)).equals(s1));
			
			// close everything
			u_end.close();
			u1_listen.socket.close();
			u1_listen.serv_socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
