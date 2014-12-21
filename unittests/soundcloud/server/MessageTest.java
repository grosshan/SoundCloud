package soundcloud.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageTest {

	@Test
	public void parseTest() {
		UserRegistry registry = new UserRegistry(2);
		try{
			Message m1 = new Message("666|F|60|50\r\n", registry);
			assertEquals(m1.getType(),Message.MessageType.Follow);
			assertEquals(m1.getNumber(),666);
			assertEquals(m1.getTarget().getID(),50);

			Message m2 = new Message("1|U|12|9\r", registry);
			assertEquals(m2.getType(),Message.MessageType.Unfollow);
			assertEquals(m2.getSource().getID(),12);
			assertEquals(m2.getPayload(),"1|U|12|9\r");

			Message m3 = new Message("542532|B\n", registry);
			assertEquals(m3.getType(),Message.MessageType.Broadcast);
			assertEquals(m3.getSource(),null);
			assertEquals(m3.getTarget(),null);

			Message m4 = new Message("43|P|32|56", registry);
			assertEquals(m4.getType(),Message.MessageType.Private);
			assertEquals(m4.getNumber(),43);
			assertEquals(m4.getTarget().getID(),56);

			Message m5 = new Message("634|S|32", registry);
			assertEquals(m5.getType(),Message.MessageType.Status);
			assertEquals(m5.getSource().getID(),32);
			assertEquals(m5.getTarget(),null);
		} catch(NumberFormatException e){
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
	}
	
	@Test
	public void parseExcTest() {
		// all messages should throw an error
		UserRegistry registry = new UserRegistry(2);
		String[] messages = {
				// 4 slot messages
				"666|F|60|50|50",
				"1|U|12",
				"A|P|32|56|31",
				// 3 slot messages
				"634|S|32|13",
				"634|S",
				// 2 slot messages
				"542532|B|23",
				"542532",
				// wrong type
				"66A|F|60|50",
				"1|U|A3|9",
				"542532|X",
				"43|P|32|B",
				"634|T|32"
		};
		int sum_err = 0;
		for(int i = 0; i < messages.length; i++){
			try{
				new Message(messages[i], registry);
			} catch (NumberFormatException e) {
				sum_err++;
			}
		}
		assertEquals(sum_err, messages.length);
	}

	@Test
	public void compareTest() {
		UserRegistry registry = new UserRegistry(2);
		try{
			Message m1 = new Message("666|F|60|50", registry);
			Message m2 = new Message("666|U|60|50", registry);
			Message m3 = new Message("30|S|60", registry);
			Message m4 = new Message("700|B", registry);
			
			assertEquals(m1,m2);
			assertEquals(m2,m1);
			assertFalse(m1.equals(m3));
			assertFalse(m3.equals(m4));
			
			assertTrue(m1.compareTo(m2) == 0);
			assertTrue(m2.compareTo(m1) == 0);
			assertTrue(m1.compareTo(m3) > 0);
			assertTrue(m3.compareTo(m2) < 0);
			assertTrue(m1.compareTo(m4) < 0);
			
		} catch (NumberFormatException e){
			e.printStackTrace();
			fail("Unexpected Exception: " + e.getMessage());
		}
	}

}
