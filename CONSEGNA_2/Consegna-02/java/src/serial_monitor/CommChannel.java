package serial_monitor;

import java.util.concurrent.BlockingQueue;

/**
 * Simple interface for an async msg communication channel
 * @author aricci
 *
 */
public interface CommChannel {
	
	/**
	 * Send a message represented by a string (without new line).
	 * 
	 * Asynchronous model.
	 * 
	 * @param msg
	 */
	void sendMsg(String msg);
	
	/**
	 * To receive a message. 
	 * 
	 * Blocking behaviour.
	 */
	String receiveMsg() throws InterruptedException;

	/**
	 * To check if a message is available.
	 * 
	 * @return
	 */
	boolean isMsgAvailable();
	
	/**
	 * Gets the queue of incoming messages.
	 * @return
	 *     a BlockingQueue representing the queue of incoming messages.
	 */
	BlockingQueue<String> getQueue();
}
