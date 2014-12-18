/**
 * A class that represents a collection of Users. Users can be registered.
 * It is also possible to search for particular Users.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */

package soundcloud.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserRegistry {

	private HashMap<Integer, User> registry;
	private ReentrantLock lock;
	
	/**
	 * standard constructor
	 */
	public UserRegistry(){
		
	}
	
	/**
	 * searches for a user with given id. Returns null if a user was not found.
	 * @param id user-id to search for
	 * @return user that corresponds to the id or null if no such user exists.
	 */
	public User getUser(int id){
		return null;
	}
	
	/**
	 * Register a new user in registry with given id. <THREAD SAFE>
	 * @param id
	 */
	public void registerUser(int id){
		
	}

	/**
	 * Searches for a specific user in the registry
	 * @param id user-id to search for
	 * @return true if a user was found, false otherwise.
	 */
	public boolean hasUser(int id){
		return getUser(id) == null;
	}
}
