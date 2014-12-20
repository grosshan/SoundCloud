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
	public Message(String payload) throws NumberFormatException{
		String[] splits = splitter.split(payload);
		
		this.payload = payload;
		if (splits.length < 2) throw new NumberFormatException("Too less fields");
		
		// extract message type
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
		number = Integer.parseInt(splits[0]);
		if (type.ordinal() < MessageType.Status.ordinal())
			source = -1;
		else
			source = Integer.parseInt(splits[2]);
		
		if (type.ordinal() < MessageType.Follow.ordinal())
			target = -1;
		else
			target = Integer.parseInt(splits[3]);
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
	 * @return source id if possible, -1 if no source is known
	 */
	public int getSource(){
		return source;
	}

	/**
	 * Get-Method for target id
	 * @return target id if possible, -1 if no concrete target is known
	 */
	public int getTarget(){
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
	 * 
	 * @param arg0 Message that will be compared.
	 */
	public int compareTo(Message arg0) {
		return this.number - arg0.number;
	}
}
