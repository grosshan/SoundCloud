/**
 * A class that represents users by the followers, stored messages
 * and - potentially - their open connections.
 * @author Michael Grosshans
 * 
 * @version 1.0
 */
package soundcloud.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class User {
	
	private int myID;

	private Socket mySocket;
	private PrintWriter myWriter;
	
	private LinkedList<Message> messages;
	private ArrayList<LinkedList<User>> followers;
	
	/**
	 * Create a user with specific id and the number of pipes that should be used
	 * @param id the id of the user
	 * @param numPipes how many pipes should be used
	 * @param numMessages the number of messages that can be stored in memory
	 */
	public User(int id, int numPipes){
		myID = id;
		messages = new LinkedList<Message>();
		followers = new ArrayList<LinkedList<User>>(numPipes);
		for(int i = 0; i < numPipes; i++){
			followers.add(i, new LinkedList<User>());
		}
		
		mySocket = null;
		myWriter = null;
	}
	
	/**
	 * Returns the id for this user.
	 * <THREAD SAFE>
	 * @return id
	 */
	public int getID(){
		return myID;
	}

	/**
	 * Returns all followers for this user for the corresponding pipe
	 * @param threadID pipe ID
	 * @return ArrayList of followers
	 */
	public LinkedList<User> getFollowers(int threadID){
		return followers.get(threadID);
	}
	
	/**
	 * Returns all stored messages for this user.
	 * @return ArrayList of followers
	 */
	public LinkedList<Message> getMessages(){
			return messages;
	}

	/**
	 * Register a connection for this user. Stored messages will be immediately flushed out.
	 * <THREAD SAFE>
	 * @param socket the socket that corresponds to the user
	 * @throws IOException Connection could not be opened.
	 */
	public void openConnection(Socket socket) throws IOException{
		synchronized(this){
			mySocket = socket;
			myWriter = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream(),"UTF-8"));
			
			while(messages.size()>0){
				myWriter.print(messages.pollFirst().getPayload());
			}
			myWriter.flush();
		}
	}
	
	/**
	 * Closes the current connection for this user.
	 * <THREAD SAFE>
	 * @throws IOException Connection could not be closed.
	 */
	public void closeConnection() throws IOException{
		synchronized(this){
			if(mySocket != null)
				mySocket.close();
			mySocket = null;
			if(myWriter != null)
				myWriter.close();
			myWriter = null;
		}
	}
	
	/**
	 * Add a follower to this user. If the user is already a follower this request will be ignored. 
	 * @param follower the new follower
	 */
	public void addFollower(User follower) {
		int pipe_id = follower.getID() % followers.size();
		if(!followers.get(pipe_id).contains(follower)){
			followers.get(pipe_id).add(follower);
		}
	}
	
	/**
	 * The given former follower will be removed.
	 * @param follower that should be removed
	 */
	public void removeFollower(User follower){
		int pipe_id = follower.getID() % followers.size();
		followers.get(pipe_id).remove(follower);
	}
	
	/**
	 * Send a specific message via an open connection.
	 * If no valid connection is open, the message will be stored in memory instead.
	 * @param message message to be sent
	 */
	public void sendMessage(Message message){
		synchronized(this){
			if(mySocket == null || mySocket.isClosed() ||
					!mySocket.isConnected() || !mySocket.isBound()){ // socket is not good, store message
				messages.add(message);
			} else {
				// send message via socket
				myWriter.print(message.getPayload());
				myWriter.flush();
			}
		}
	}

	/**
	 * Check if two users are equal. Two Users are equal if they have the same id.
	 * <THREAD SAFE>
	 * @param o An object to compare with.
	 * @return false if o is not a user, or a different user.
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof User)) return false;
		return ((User)o).myID == this.myID;
	}
}
