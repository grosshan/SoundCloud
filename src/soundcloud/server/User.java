/**
 * A class that represents users by the followers, person-to-follow, stored messages
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
	private ArrayList<User> personsOfInterest;
	
	public User(int id, int numMessages){
		myID = id;
		
		messages = new ArrayDeque<Message>(numMessages);
		followers = new ArrayList<User>();
		personsOfInterest = new ArrayList<User>();
	}
	
	public void openConnection(Socket socket){
		mySocket = socket;
	}
	
	public void addFollower(User follower){
		
	}
	
	public void addPOI(User personOfInterest){
		
	}
	
	public void removerFollower(User follower){
		
	}
	
	public void removePOI(User personOfInterest){
		
	}
	
	public ArrayList<User> getFollowers(){
		return null;
	}
	
	public ArrayList<User> getPOIs(){
		return null;
	}
	
	public void sendMessage(Message message){
		
	}
}
