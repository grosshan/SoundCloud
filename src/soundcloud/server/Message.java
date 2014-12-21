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
		Broadcast, Status, Follow, Unfollow, Private;
	}
	
	static final Pattern splitter= Pattern.compile("[\\|\r\n]");
	
	// parsed information
	private final MessageType type;
	private final int number;
	private User source;
	private User target;
	
	// pure message
	private String payload;
	
	/**
	 * Creates a message object given a specific string representation of it.
	 * @param payload pure string representation of the message
	 */
	public Message(String payload, UserRegistry registry) throws NumberFormatException{
		String[] splits = splitter.split(payload);
		
		this.payload = payload;
		if (splits.length < 2) throw new NumberFormatException("Too less fields");
		
		// extract message type & check number of fields
		switch(splits[1]){
			case "F":
				type = MessageType.Follow;
				if (splits.length != 4) throw new NumberFormatException("wrong number of fields");
				
				break;
			case "U":
				type = MessageType.Unfollow;
				if (splits.length != 4) throw new NumberFormatException("wrong number of fields");
				break;
			case "B":
				type = MessageType.Broadcast;
				if (splits.length != 2) throw new NumberFormatException("wrong number of fields");
				break;
			case "S":
				type = MessageType.Status;
				if (splits.length != 3) throw new NumberFormatException("wrong number of fields");
				break;
			case "P":
				type = MessageType.Private;
				if (splits.length != 4) throw new NumberFormatException("wrong number of fields");
				break;
			default:
				throw new NumberFormatException("Unknown Message Type");
		}
		// build source
		number = Integer.parseInt(splits[0]);
		
		if (type.ordinal() < MessageType.Status.ordinal()) // Broadcast -> no source
			source = null;
		else {
			int source_id = Integer.parseInt(splits[2]);
			this.source = registry.getUser(source_id);
			if(this.source == null){
				this.source = registry.registerUser(source_id);
			}
		}
		
		// build target
		if (type.ordinal() < MessageType.Follow.ordinal()) // Broadcast && Status -> no target
			target = null;
		else {
			int target_id = Integer.parseInt(splits[3]);
			this.target = registry.getUser(target_id);
			if(this.target == null){
				this.target = registry.registerUser(target_id);
			}
		}
	}
	
	/**
	 * Get-Method for message type
	 * @return message type
	 */
	public MessageType getType(){
		return type;
	}

	/**
	 * Get-Method for sequence number
	 * @return sequence number
	 */
	public int getNumber(){
		return number;
	}

	/**
	 * Get-Method for source id
	 * @return source if possible, null if no source is known
	 */
	public User getSource(){
		return source;
	}

	/**
	 * Get-Method for target id
	 * @return target if possible, null if no concrete target is known
	 */
	public User getTarget(){
		return target;
	}

	/**
	 * Get-Method for pure string representation
	 * @return pure representation
	 */
	public String getPayload(){
		return payload;
	}

	/**
	 * Determines if two messages or a message and an Integer are equal. 
	 * More formally, two messages are equal, if and only if they have the same sequence number.
	 * More formally, a messages and an integer are equal, if the sequence number corresponds to the integer.
	 * <THREAD SAFE>
	 */
	public boolean equals(Object o){
		if(o instanceof Message) return ((Message)o).number == this.number;
		if(o instanceof Integer) return ((Integer)o).intValue() == this.number;
		return false;
	}

	
	@Override
	/**
	 * Message to compare the sequence number of two messages. Returns a ret_val, where
	 * if #this < #arg0  then ret_val < 0
	 * if #this == #arg0 then ret_val == 0
	 * if #this > #arg0  then ret_val > 0
	 * <THREAD SAFE>
	 * @param arg0 Message that will be compared.
	 */
	public int compareTo(Message arg0) {
		return this.number - arg0.number;
	}
}
