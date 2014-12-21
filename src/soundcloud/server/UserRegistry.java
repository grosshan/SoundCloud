/**
 * A class that represents a collection of Users. Users can be registered.
 * It is also possible to search for particular Users.
 * 
 * @author Michael Grosshans
 * @version 1.0
 */

package soundcloud.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserRegistry {

	private ArrayList<HashMap<Integer, User>> registry;
	
	/**
	 * standard constructor
	 */
	public UserRegistry(int numPipes){
		new ReentrantLock();
		registry = new ArrayList<HashMap<Integer, User>>(numPipes);
		for(int i = 0; i < numPipes; i++){
			registry.add(new HashMap<Integer, User>(10000));
		}
	}
	
	/**
	 * Return a list of all users in registry for the specific pipe.
	 * @param threadID the pipe id that wants to take care of these users
	 * @return user that corresponds to the id or null if no such user exists.
	 */
	public Collection<User> getAllUser(int threadID){
		return registry.get(threadID).values();
	}

	/**
	 * searches for a user with given id. Returns null if a user was not found.
	 * @param id user-id to search for
	 * @return user that corresponds to the id or null if no such user exists.
	 */
	public User getUser(int id){
		int t_pipe = id % registry.size();
		return registry.get(t_pipe).get(id);
	}
	
	/**
	 * Register a new user in registry with given id. 
	 * If a user with same id already exists, nothing will be done.
	 * <THREAD SAFE>
	 * @param id user-id
	 * @return user that was registered
	 */
	public User registerUser(int id){
		return registerUser(new User(id, this.registry.size()));
	}

	/**
	 * Register a new user in registry. If a user with same id already exists, nothing will be done.
	 * <THREAD SAFE>
	 * @param user user that should be registered
	 * @return user that was registered
	 */
	public User registerUser(User user){
		int t_pipe = user.getID() % registry.size();
		User ret;
		synchronized(registry.get(t_pipe)){
			ret = registry.get(t_pipe).get(user.getID());
			
			if(ret == null) {
				registry.get(t_pipe).put(user.getID(), user);
				ret = user;
			}
		}
		return ret;			
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
