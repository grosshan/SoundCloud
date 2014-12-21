package soundcloud.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageCollectorTest {

	@Test
	public void orderTest() {

		User u1 = new User(1);
		User u2 = new User(2);
		UserRegistry registry = new UserRegistry();
		registry.registerUser(u1);
		registry.registerUser(u2);

		MessageQueue queue = new MessageQueue();
		for(int i = 98; i >= 0; i--){
			String s = (i+1) + "|";
			
			if (i%2 == 0)
				s += "F|";
			else
				s += "U|";
			
			s += "1|2";
			
			Message m = null;
			try{
				m = new Message(s, registry);
			} catch (Exception e){
				e.printStackTrace();
				fail("Unexpected Exception: " + e.getMessage());
			}
			queue.offer(m);
		}
		
		assertTrue(queue.size() == 99);
		
		MessageCollector collect = new MessageCollector(queue, registry, 4);
		collect.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
		collect.stop();
		
		assertNotNull(u1.getMessages());
		assertNotNull(u2.getMessages());
		
		assertTrue(u1.getMessages().size() == 0);
		assertTrue(u2.getMessages().size() == 50);
		int i = 0;
		while(u2.getMessages().size()>0){
			assertTrue(u2.getMessages().pollFirst().getNumber() == 2*i+1);
			i++;
		}
		assertTrue(u2.getMessages().size() == 0);
		
	}

	@Test
	public void msgTest() {
		
		User u1 = new User(1);
		User u2 = new User(2);
		User u3 = new User(3);
		UserRegistry registry = new UserRegistry();
		registry.registerUser(u1);
		registry.registerUser(u2);
		registry.registerUser(u3);

		MessageQueue queue = new MessageQueue();
		try{
			// follow/unfollow
			queue.offer(new Message("1|F|2|1", registry));
			queue.offer(new Message("2|F|3|2", registry));
			queue.offer(new Message("3|F|3|1", registry));
			queue.offer(new Message("4|F|1|2", registry));
			queue.offer(new Message("5|U|1|2", registry));
			
			// priv
			queue.offer(new Message("6|P|2|1", registry));
			queue.offer(new Message("7|P|1|2", registry));
			queue.offer(new Message("8|P|2|3", registry));
			
			// status
			queue.offer(new Message("9|S|1", registry));
			queue.offer(new Message("10|S|2", registry));
			queue.offer(new Message("11|S|3", registry));

			// broad
			queue.offer(new Message("12|B", registry));
		} catch (Exception e){
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
		
		assertTrue(queue.size() == 12);
		
		MessageCollector collect = new MessageCollector(queue, registry, 4);
		collect.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
		collect.stop();
		
		assertNotNull(u1.getMessages());
		assertTrue(u1.getMessages().size() == 4);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 1);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 3);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 6);
		assertTrue(u1.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u1.getFollowers().size() == 2);

		assertNotNull(u2.getMessages());
		assertTrue(u2.getMessages().size() == 5);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 2);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 4);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 7);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 9);
		assertTrue(u2.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u2.getFollowers().size() == 1);

		assertNotNull(u3.getMessages());
		assertTrue(u3.getMessages().size() == 4);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 8);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 9);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 10);
		assertTrue(u3.getMessages().pollFirst().getNumber() == 12);
		assertTrue(u3.getFollowers().size() == 0);

	}

}
