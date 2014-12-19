package soundcloud.server;

import static org.junit.Assert.*;

import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Test;

public class MessageCollectorTest {

	@Test
	public void orderTest() {
		
		User u1 = new User(1,100);
		User u2 = new User(2,100);
		UserRegistry registry = new UserRegistry();
		registry.registerUser(u1);
		registry.registerUser(u2);

		PriorityBlockingQueue<Message> queue = new PriorityBlockingQueue<Message>();
		for(int i = 98; i >= 0; i--){
			String s = (i+1) + "|";
			
			if (i%2 == 0)
				s += "F|";
			else
				s += "U|";
			
			s += "1|2";
			
			Message m = null;
			try{
				m = new Message(s);
			} catch (Exception e){
				fail(e.getMessage());
			}
			queue.add(m);
		}
		
		assertTrue(queue.size() == 99);
		assertTrue(queue.peek().getNumber() == 1);
		
		MessageCollector collect = new MessageCollector(queue, registry, 4);
		collect.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		collect.stop();
		
		assertTrue(u1.getMessages().size() == 100);
		for(int i = 0; i < u1.getMessages().size(); i++){
			assertTrue(u1.getMessages().pollFirst().getNumber() == i+1);
		}
		assertTrue(u1.getMessages().size() == 0);
		
	}

	@Test
	public void msgTest() {
		
		User u1 = new User(1,100);
		User u2 = new User(2,100);
		User u3 = new User(3,100);
		UserRegistry registry = new UserRegistry();
		registry.registerUser(u1);
		registry.registerUser(u2);
		registry.registerUser(u3);

		PriorityBlockingQueue<Message> queue = new PriorityBlockingQueue<Message>();
		try{
			// follow/unfollow
			queue.add(new Message("1|F|2|1"));
			queue.add(new Message("2|F|3|2"));
			queue.add(new Message("3|F|3|1"));
			queue.add(new Message("4|F|1|2"));
			queue.add(new Message("5|U|1|2"));
			
			// priv
			queue.add(new Message("6|P|2|1"));
			queue.add(new Message("7|P|1|2"));
			queue.add(new Message("8|P|2|3"));
			
			// status
			queue.add(new Message("9|S|1"));
			queue.add(new Message("10|S|2"));
			queue.add(new Message("11|S|3"));

			// broad
			queue.add(new Message("12|B"));
		} catch (Exception e){
			fail(e.getMessage());
		}
		
		assertTrue(queue.size() == 12);
		assertTrue(queue.peek().getNumber() == 1);
		
		MessageCollector collect = new MessageCollector(queue, registry, 4);
		collect.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		collect.stop();
		
		assertTrue(u1.getMessages().size() == 4);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 1);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 3);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 6);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u1.getFollowers().size() == 2);

		assertTrue(u2.getMessages().size() == 6);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 2);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 4);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 5);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 7);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 9);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u2.getFollowers().size() == 1);

		assertTrue(u3.getMessages().size() == 4);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 8);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 9);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 10);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u3.getFollowers().size() == 0);

	}

}
