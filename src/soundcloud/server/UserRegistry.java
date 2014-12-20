/**
 * A class that represents a collection of Users. Users can be registered.
 * It is also possible to search for particular Users.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */

package soundcloud.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserRegistry {

	private HashMap<Integer, User> registry;
	private ReentrantLock lock;
	
	/**
	 * standard constructor
	 */
	public UserRegistry(){
		lock = new ReentrantLock();
		registry = new HashMap<Integer, User>();
	}
	
	/**
	 * Return a list of all users in registry.
	 * <THREAD SAFE>
	 * @return user that corresponds to the id or null if no such user exists.
	 */
	public Collection<User> getAllUser(){
		return registry.values();
	}

	/**
	 * searches for a user with given id. Returns null if a user was not found.
	 * <THREAD SAFE>
	 * @param id user-id to search for
	 * @return user that corresponds to the id or null if no such user exists.
	 */
	public User getUser(int id){
		return registry.get(id);
	}
	
	/**
	 * Register a new user in registry with given id. If a user with same id already exists, nothing will be done.
	 * <THREAD SAFE>
	 * @param id user-id
	 * @return user that was registered
	 */
	public User registerUser(int id){
		return registerUser(new User(id));
	}

	/**
	 * Register a new user in registry. If a user with same id already exists, nothing will be done.
	 * <THREAD SAFE>
	 * @param user user that should be registered
	 * @return user that was registered
	 */
	public User registerUser(User user){
		lock.lock();
		if(getUser(user.getID()) != null) 
			return getUser(user.getID());
		else 
			registry.put(user.getID(), user);
		lock.unlock();
		return user;
	}
	/**
	 * Searches for a specific user in the registry
	 * <THREAD SAFE>
	 * @param id user-id to search for
	 * @return true if a user was found, false otherwise.
	 */
	public boolean hasUser(int id){
		return getUser(id) != null;
	}
}
