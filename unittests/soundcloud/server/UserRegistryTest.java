package soundcloud.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserRegistryTest {

	@Test
	public void registerTest() {
		User u1 = new User(1);
		User u2 = new User(2);
		User u3 = new User(3);
		UserRegistry registry = new UserRegistry();
		registry.registerUser(1);
		registry.registerUser(u2);
		registry.registerUser(u3);
		
		assertNotNull(registry.getAllUser());
		assertTrue(registry.getAllUser().size() == 3);
		assertTrue(registry.getAllUser().contains(u1));
		assertTrue(registry.getAllUser().contains(u2));
		assertTrue(registry.getAllUser().contains(u3));

		assertFalse(registry.getUser(1) == u1);
		assertTrue(registry.getUser(2) == u2);
		assertTrue(registry.getUser(3) == u3);
	
		assertTrue(registry.getUser(1).equals(u1));
		assertTrue(registry.getUser(2).equals(u2));
		assertTrue(registry.getUser(3).equals(u3));

		assertTrue(registry.hasUser(1));
		assertTrue(registry.hasUser(2));
		assertTrue(registry.hasUser(3));
	}

}
