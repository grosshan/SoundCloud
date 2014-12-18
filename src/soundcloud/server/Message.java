/**
 * A class that represents a message in the SoundCloud Project
 * 
 * @author Michael Grosshans
 * @version 1.0
 */
package soundcloud.server;

import java.util.regex.Pattern;

public class Message implements Comparable<Message>{

	/**
	 * A enumeration of all possible message types.
	 * 
	 * @author grosshan
	 * @version 1.0
	 */
	public enum MessageType{
		Follow, Unfollow, Broadcast, Private, Status;
	}
	
	static final Pattern splitter= Pattern.compile("\\|");
	
	// parsed information
	private MessageType type;
	private int number;
	private int source;
	private int target;
	
	// pure message
	private String payload;
	
	/**
	 * Creates a message object given a specific string representation of it.
	 * @param payload pure string representation of the message
	 */
	public Message(String payload){
		
	}
	
	/**
	 * Determines if two messages are equal. More formally, two messages are equal, if and only if they have the
	 * same sequence number.
	 */
	public boolean equals(Object o){
		return false;
	}

	
	@Override
	/**
	 * Message to compare the sequence number of two messages. Returns a ret_val, where
	 * if #this < #arg0  then ret_val < 0
	 * if #this == #arg0 then ret_val == 0
	 * if #this > #arg0  then ret_val > 0
	 * 
	 * @param arg0 Message that will be compared.
	 */
	public int compareTo(Message arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
