package radar.raspi.emu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;

import radar.raspi.common.Observable;
import radar.raspi.main.PositionInfoMsg;
import radar.raspi.serial.Serial;

public class SerialImpl extends Observable implements Serial {

	private int rx;
	private int tx;
	private boolean isMsgAvailable;
	private String msg;
	private StdInputAgent auxWorker;
	
	public SerialImpl(int rx, int tx){
		this.rx = rx;
		this.tx = tx;
		try {
			auxWorker = new StdInputAgent();
			auxWorker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public synchronized boolean isMsgAvailable() {
//		return isMsgAvailable;
//	}
	
	public synchronized String readMsg(){
		String m = msg;
		isMsgAvailable = false;
		msg = null;
		return m;
	}
	
	public synchronized void sendMsg(String msg){
		System.out.println(msg);
	}
	
	synchronized void setMsg(String msg){
		this.isMsgAvailable = true;
		this.msg = msg;
		notifyAll();
	}
	
	public synchronized String waitForMsg() throws InterruptedException {
			while (!isMsgAvailable){
				wait();
			}
			String m = msg;
			isMsgAvailable = false;
			msg = null;
			return m;
	}
	
	private static PositionInfoMsg createPositionInfoMsg(String in) {
            String[] parts = in.split(";");
            return new PositionInfoMsg(Integer.parseInt(parts[0]), Float.parseFloat(parts[1]));
        }
	
	class StdInputAgent extends Thread {
		public StdInputAgent(){
		}
		
		public void run(){
			System.out.println("[StdInput Emu Agent] running.");
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			while (true){
				try {
					String msg = input.readLine();
					setMsg(msg);
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
