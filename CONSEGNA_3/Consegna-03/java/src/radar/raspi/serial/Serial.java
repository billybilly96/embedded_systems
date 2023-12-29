package radar.raspi.serial;

public interface Serial {
	
	/**
         * Send a message represented by a string (without new line).
         * 
         * Asynchronous model.
         * 
         * @param msg
         */
	void sendMsg(String msg);
}
