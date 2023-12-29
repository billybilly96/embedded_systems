package radar.raspi.emu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import radar.raspi.devices.ButtonPressed;
import radar.raspi.devices.ButtonReleased;

public class ObservableButton extends radar.raspi.devices.ObservableButton {

	private int pinNum;
	private boolean isPressed;
	private ButtonFrame buttonFrame;
	private String label;
	
	public ObservableButton(int pinNum, String label){
		this.pinNum = pinNum;
		this.label = label;
		try {
			buttonFrame = new ButtonFrame(pinNum, label);
			buttonFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized boolean isPressed() {
		return isPressed;
	}
	
	void setPressed(boolean state){
		isPressed = state;
		if (isPressed){
			this.notifyEvent(new ButtonPressed(this));
			System.out.println("BTN_" + label + " PRESSED. EVENT FIRED.");
		} else {
			this.notifyEvent(new ButtonReleased(this));
		}
	}
	
	class ButtonFrame extends JFrame implements MouseListener {
		  public ButtonFrame(int pin, String label){
		    super("Button Emu");
		    setSize(200,50);
		    JButton button = new JButton(label + " [PIN #"+ pin + "]");
		    button.addMouseListener(this);
		    getContentPane().add(button);
		    addWindowListener(new WindowAdapter(){
		      public void windowClosing(WindowEvent ev){
		        System.exit(-1);
		      }
		    });
		  }

		@Override
		public void mousePressed(MouseEvent e) {
			setPressed(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setPressed(false);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {			
		}
	}

}
