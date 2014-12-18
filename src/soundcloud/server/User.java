/**
 * A class that represents users by the followers, stored messages
 * and - potentially - their open connections.
 * @author Michael Grosshans
 * 
 * @version 1.0
 */
package soundcloud.server;

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
	 * lock this user
	 */
	public void lockUser(){
		
	}
	
	/**
	 * unlock this user
	 */
	public void unlockUser(){
		
	}
	
	/**
	 * Register a connection for this user. Stored messages will be immediately flushed out.
	 * @param socket the socket that corresponds to the user
	 */
	public void openConnection(Socket socket){
		mySocket = socket;
	}
	
	/**
	 * Add a follower to this user. 
	 * @param follower the new follower
	 */
	public void addFollower(User follower){
		
	}
	
	/**
	 * The given former follower will be removed
	 * @param follower that should be removed
	 */
	public void removerFollower(User follower){
		
	}
	
	/**
	 * Returns all followers for this user.
	 * @return ArrayList of followers
	 */
	public ArrayList<User> getFollowers(){
		return null;
	}
	
	/**
	 * Send a specific message via an open connection.
	 * If no connection is open, the message will be stored in memory instead.
	 * @param message message to be sent
	 */
	public void sendMessage(Message message){
		
	}
}
