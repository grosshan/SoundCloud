/**
 * A class that represents users by the followers, stored messages
 * and - potentially - their open connections.
 * @author Michael Grosshans
 * 
 * @version 1.0
 */
package soundcloud.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class User {
	
	private int myID;

	private Socket mySocket;
	private ReentrantLock myLock;
	
	private ArrayDeque<Message> messages;
	private ArrayList<User> followers;
	
	/**
	 * Create a user with specific id and the maximal amount of messages that can be stored memory.
	 * @param id the id of the user
	 * @param numMessages the number of messages that can be stored in memory
	 */
	public User(int id, int numMessages){
		myID = id;
		
		messages = new ArrayDeque<Message>(numMessages);
		followers = new ArrayList<User>();
	}

	/**
	 * Create a user with specific id and the maximal amount of 30 messages that can be stored memory.
	 * @param id the id of the user
	 */
	public User(int id){
		this(id, 30);
	}
	
	/**
	 * Returns all followers for this user.
	 * @return ArrayList of followers
	 */
	public ArrayList<User> getFollowers(){
		return null;
	}
	
	/**
	 * Returns all stored messages for this user.
	 * @return ArrayList of followers
	 */
	public ArrayDeque<Message> getMessages(){
		return null;
	}

	/**
	 * lock this user
	 */
	public void lock(){

	}
	
	/**
	 * unlock this user
	 */
	public void unlock(){
		
	}
	
	/**
	 * Check if this user is locked or not
	 * return true, if and only if the user is locked
	 */
	public boolean isLocked(){
		return false;
	}

	/**
	 * Register a connection for this user. Stored messages will be immediately flushed out.
	 * @param socket the socket that corresponds to the user
	 * @throws IOException Connection could not be opened.
	 */
	public void openConnection(Socket socket) throws IOException{
		mySocket = socket;
	}
	
	/**
	 * Closes the current connection for this user.
	 * @throws IOException Connection could not be closed.
	 */
	public void closeConnection() throws IOException{
		
	}
	/**
	 * Add a follower to this user. If the user is already a follower this request will be ignored. 
	 * @param follower the new follower
	 */
	public void addFollower(User follower) {
		
	}
	
	/**
	 * The given former follower will be removed.
	 * @param follower that should be removed
	 */
	public void removeFollower(User follower){
		
	}
	
	/**
	 * Send a specific message via an open connection.
	 * If no valid connection is open, the message will be stored in memory instead.
	 * @param message message to be sent
	 */
	public void sendMessage(Message message){
		
	}

	/**
	 * Check if two users are equal. Two Users are equal if they have the same id.
	 * @param o An object to compare with.
	 * @return false if o is not a user, or a different user.
	 */
	@Override
	public boolean equals(Object o){
		return false;
		
	}
}
