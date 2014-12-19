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
	public void lockTest(){
		User u1 = new User(1);
		
		assertFalse(u1.isLocked());
		u1.lock();
		assertTrue(u1.isLocked());
		u1.unlock();		
		assertFalse(u1.isLocked());
	}

	@Test
	public void followerTest(){
		User u1 = new User(1);
		User u2 = new User(2);
		User u3 = new User(2);
		
		assertNotNull(u1.getFollowers());
		assertTrue(u1.getFollowers().size() == 0);
		u1.addFollower(u1);
		assertTrue(u1.getFollowers().size() == 1);
		u1.addFollower(u2);
		u1.addFollower(u3);
		assertTrue(u1.getFollowers().size() == 2);
		assertTrue(u1.getFollowers().get(0) == u2 || 
					u1.getFollowers().get(1) == u2);
		assertTrue(u1.getFollowers().get(0) == u1 || 
				u1.getFollowers().get(1) == u1);
		
		u1.removeFollower(u3);
		assertTrue(u1.getFollowers().size() == 1);
		u1.removeFollower(u2);
		assertTrue(u1.getFollowers().size() == 1);
		u1.removeFollower(u1);
		assertTrue(u1.getFollowers().size() == 0);
		
	}

	@Test
	public void messageTest() {
		
		MyListener u1_listen;
		try {
			u1_listen = new MyListener();
			new Thread(u1_listen).start();
			
			Socket u_end = new Socket("localhost",u1_listen.port);
			BufferedReader reader = new BufferedReader(new InputStreamReader(u_end.getInputStream()));
			User u1 = new User(1,30);

			// store messages
			String s1 = "1|F|1|1";
			String s2 = "2|F|1|1";
			String s3 = "3|F|1|1";
			u1.sendMessage(new Message(s1));
			u1.sendMessage(new Message(s2));
			u1.sendMessage(new Message(s3));
			
			assertNotNull(u1.getMessages());
			assertTrue(u1.getMessages().size() == 3);
			assertTrue(u1.getMessages().peekFirst().getNumber() == 1);
			
			u1.openConnection(u_end);
			assertNotNull(u1.getMessages());
			assertTrue(u1.getMessages().size() == 0);
			
			assertTrue(reader.readLine().equals(s1));
			assertTrue(reader.readLine().equals(s2));
			assertTrue(reader.readLine().equals(s3));

			u1.sendMessage(new Message(s1));
			assertTrue(reader.readLine().equals(s1));
			
			u_end.close();
			u1_listen.socket.close();
			u1_listen.serv_socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
