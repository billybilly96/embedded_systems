package application;

import com.sun.javafx.application.PlatformImpl;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import serial_monitor.CommChannel;

public class ParkingAssistance implements Runnable{

    private CommChannel monitor;
    private Gui gui;
    private boolean loop;
    private String msg;
    
    public ParkingAssistance (CommChannel monitor, Gui gui) {
        this.monitor = monitor;
        this.gui = gui;
    }
    
    @Override
    public void run() {
        //String msg = null;
        loop = true;
        while (loop) {
            System.out.println("Parking");
            try {
                msg = monitor.receiveMsg();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PlatformImpl.runAndWait(() -> this.gui.appendText(msg));
            if (msg.equals("OK")) {
                this.loop = false;
//                PlatformImpl.runAndWait(() -> {
//                    this.gui.setText(".::Car OnBoard PC::.");
//                    this.gui.setStopEnabled(false);
//                    this.gui.setArrivingEnabled(true);
//                    System.out.println("Dovrei avere riabilitato!");
//                });
                PlatformImpl.runAndWait(() -> this.gui.appendText("[PARK_ASSIST_GUI] Closing door\n"));
                PlatformImpl.runAndWait(() -> gui.resetScene());
//                System.out.println("Dovrei avere riabilitato!");
            }
        }
    }

}
