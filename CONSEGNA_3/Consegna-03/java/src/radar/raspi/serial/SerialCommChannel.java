package radar.raspi.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import radar.raspi.common.MsgEvent;
import radar.raspi.common.Observable;
import radar.raspi.main.PositionInfoMsg;
import radar.raspi.serial_test.TestMsg;

import java.io.*;

/**
 * Comm channel implementation based on serial port.
 * 
 * @author aricci
 *
 */
public class SerialCommChannel extends Observable implements Serial, SerialPortEventListener {

	private SerialPort serialPort;
	private BufferedReader input;
	private OutputStream output;
	//private BlockingQueue<String> queue;

	public SerialCommChannel(String port, int rate) throws Exception {
		//queue = new ArrayBlockingQueue<String>(100);

		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
		// open serial port, and use class name for the appName.
		SerialPort serialPort = (SerialPort) portId.open(this.getClass().getName(), 2000);

		// set port parameters
		serialPort.setSerialPortParams(rate,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		
//		serialPort.setSerialPortParams(rate,
//                        SerialPort.DATABITS_7,
//                        SerialPort.STOPBITS_1,
//                        SerialPort.PARITY_EVEN);

		// serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);

	}

	@Override
	public void sendMsg(String msg) {
		char[] array = (msg+"\n").toCharArray();
		byte[] bytes = new byte[array.length];
		for (int i = 0; i < array.length; i++){
			bytes[i] = (byte) array[i];
		}
		try {
			output.write(bytes);
			output.flush();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

//	@Override
//	public String receiveMsg() throws InterruptedException {
//		// TODO Auto-generated method stub
//		return queue.take();
//	}
//
//	@Override
//	public boolean isMsgAvailable() {
//		// TODO Auto-generated method stub
//		return !queue.isEmpty();
//	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String in=input.readLine();
				notifyEvent(new MsgEvent(createPositionInfoMsg(in)));
//				notifyEvent(new MsgEvent(new TestMsg(in)));
				//queue.put(msg);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

//    @Override
//    public BlockingQueue<String> getQueue() {
//        return this.queue;
//    }
	
	private static PositionInfoMsg createPositionInfoMsg(String in) {
	    String[] parts = in.split(";");
            return new PositionInfoMsg(Integer.parseInt(parts[0]), Float.parseFloat(parts[1]));
	}
}
