/**
 * A class that represents a message in the SoundCloud Project
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

public class Message {

	public enum MessageType{
		Follow, Unfollow, Broadcast, Private, Status;
	}
	
	private MessageType type;
	private int number;
	private int source;
	private int target;
	private String payload;
	
	public Message(String payload){
		
	}
	
	public boolean equals(Object o){
		return false;
	}
}
