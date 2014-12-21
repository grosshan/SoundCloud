package soundcloud.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserRegistryTest {

	@Test
	public void registerTest() {
		// build registry
		User u1 = new User(1,2);
		User u2 = new User(2,2);
		User u3 = new User(3,2);
		UserRegistry registry = new UserRegistry(2);
		registry.registerUser(1);
		registry.registerUser(u2);
		registry.registerUser(u3);
		
		assertNotNull(registry.getAllUser(0));
		assertNotNull(registry.getAllUser(1));
		assertTrue(registry.getAllUser(0).size() == 1);
		assertFalse(registry.getAllUser(0).contains(u1));
		assertTrue(registry.getAllUser(0).contains(u2));
		assertFalse(registry.getAllUser(0).contains(u3));
		assertTrue(registry.getAllUser(1).size() == 2);
		assertTrue(registry.getAllUser(1).contains(u1));
		assertFalse(registry.getAllUser(1).contains(u2));
		assertTrue(registry.getAllUser(1).contains(u3));

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
