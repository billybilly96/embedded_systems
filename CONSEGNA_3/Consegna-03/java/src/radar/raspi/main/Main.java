package radar.raspi.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import radar.raspi.devices.Light;
import radar.raspi.devices.ObservableButton;
import radar.raspi.pi4j_impl.Button;
import radar.raspi.pi4j_impl.Led;
import radar.raspi.serial.SerialCommChannel;
import radar.raspi.serial_test.TestSerial;


public class Main {

    private static String commPort;
    private static int dataRate;
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("args: <CommPortName> <BaudRate>");
            System.exit(1);
        }
        commPort = args[0];
        dataRate = Integer.parseInt(args[1]);
        System.err.println("Start monitoring port " + commPort + " at " + dataRate + " baud");
        
//      ------------- MOCK IMPLEMENTATION ----------------
        
//        final Light ledOn = new radar.raspi.emu.Led(13, "LED_ON");
//        final Light ledTracking = new radar.raspi.emu.Led(12, "LED_TRACKING");
//        final Light ledDetected = new radar.raspi.emu.Led(11, "LED_DETECTED");
//        final ObservableButton btnOn = new radar.raspi.emu.ObservableButton(10, "ON");
//        final ObservableButton btnOff = new radar.raspi.emu.ObservableButton(9, "OFF");
        
//      ---------------------------------------------------
        
        
//        -----------REAL Pi4j IMPLEMENTATION -----------
        
        final Light ledOn = new Led(12);
        final Light ledDetected = new Led(13);
        final Light ledTracking = new Led(14);
        final ObservableButton btnOn = new Button(10);
        final ObservableButton btnOff = new Button(11);
        
//        ------------------------------------------------
        
        final String logPath = System.getProperty("user.home") + System.getProperty("file.separator") + "smart_radar.log";
        final PrintStream logger = new PrintStream(new FileOutputStream(logPath, true), true);
        
        final RadarController controller = new RadarController(ledOn, ledDetected, ledTracking, btnOn, btnOff, logger);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(4000);
        System.out.println("Ready.");
        controller.start();
        
//        ---------- VIRTUAL SERIAL PORT TEST --------------
//        
//        final TestSerial test = new TestSerial();
//        test.start();
//        
//        --------------------------------------------------
        
    }
    
    public static SerialCommChannel getSerialCommChannel() {
        try {
            return new SerialCommChannel(commPort, dataRate);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
