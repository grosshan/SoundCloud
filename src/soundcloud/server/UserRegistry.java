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
	
	public User getUser(int id){
		return null;
	}
	
	public void registerUser(int id, Socket socket){
		
	}
	
	public void registerUser(int id){
		
	}

	public boolean hasUser(int id){
		return getUser(id) == null;
	}
}
