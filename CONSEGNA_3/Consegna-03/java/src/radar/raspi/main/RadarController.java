package radar.raspi.main;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import radar.raspi.common.BasicEventLoopController;
import radar.raspi.common.Event;
import radar.raspi.common.MsgEvent;
import radar.raspi.devices.ButtonPressed;
import radar.raspi.devices.Light;
import radar.raspi.devices.ObservableButton;
import radar.raspi.pi4j_impl.Button;
import radar.raspi.serial.SerialCommChannel;
import radar.raspi.common.Msg;

public class RadarController extends BasicEventLoopController {
    
    private static final float DIST_MIN = 0.2f;
    private static final float DIST_MAX = 2f;
    private static final int MIN_ANGLE = 0;
    private static final int MAX_ANGLE = 180;
    private static final float DIST_DETECTED = 0.5f;
    private static final long FLASH_DURATION = 100;
    private static final DateFormat PRINT_DF = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat LOG_DF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    private enum State {IDLE, SCANNING, TRACKING};
    
    private PrintStream logger;
    private State currentState;
    private SerialCommChannel serial;
    private int detected;
    private Light ledDetected;
    private ObservableButton buttonOn;
    private ObservableButton buttonOff;
    private Light ledTracking;
    private Light ledOn;
    private float prevDist;
    
    public RadarController(Light ledOn, Light ledDetected, Light ledTracking, ObservableButton btnOn, ObservableButton btnOff, PrintStream logger) {
        this.ledDetected = ledDetected;
        this.ledTracking = ledTracking;
        this.ledOn = ledOn;
        this.buttonOn = btnOn;
        this.buttonOff = btnOff;
        this.logger = logger;
        this.serial = Main.getSerialCommChannel();
        this.serial.addObserver(this);
        this.buttonOn.addObserver(this);
        this.buttonOff.addObserver(this);
        this.detected = 0;
        this.prevDist = DIST_MAX + 2;
        this.currentState = State.IDLE;
    }
    
    @Override
    protected void processEvent(Event ev) {
        switch (currentState){
        case IDLE:
            if (ev instanceof ButtonPressed){
               if (((ButtonPressed) ev).getSourceButton().equals(buttonOn)){
                   //System.out.println("BTN_ON PRESSED. SENDING MESSAGE.");
                   serial.sendMsg("i");
                   switchOn(ledOn);
                   this.detected = 0;
                   this.prevDist = DIST_MAX + 2;
                   currentState = State.SCANNING;
                }
            }
            break;
            
        case SCANNING:
            if (ev instanceof MsgEvent){
                Msg msg = ((MsgEvent) ev).getMsg();
                if (msg instanceof PositionInfoMsg){
                    int angle = ((PositionInfoMsg) msg).getAngle();
                    float distance = ((PositionInfoMsg) msg).getDistance();
                    
                    if (distance <= DIST_DETECTED && prevDist > DIST_DETECTED) {
                            detected++;
                            printAndLogDetected(angle);
                            flash(ledDetected, FLASH_DURATION);
                    }
                    
                    if(angle == MAX_ANGLE || angle == MIN_ANGLE) {
                        printAndLogScanComplete(detected);
                        detected = 0;
                    }
                    
                    if (distance <= DIST_MIN) {
                        currentState = State.TRACKING;
                        switchOn(ledTracking);
                        serial.sendMsg("s");
                       // prevDist = distance;
                       printAndLogTracked(angle, distance);
                    }
                    prevDist = distance;
                }
            } else if (ev instanceof ButtonPressed){
                if (((ButtonPressed) ev).getSourceButton().equals(buttonOff)) {
                    serial.sendMsg("o");
                    switchOff(ledOn);
                    currentState = State.IDLE;
                }
              }
            break;
            
        case TRACKING: 
            if (ev instanceof MsgEvent){
                Msg msg = ((MsgEvent) ev).getMsg();
                if (msg instanceof PositionInfoMsg){
                    float distance = ((PositionInfoMsg) msg).getDistance();
                    int angle = ((PositionInfoMsg) msg).getAngle();
                    if (distance >= DIST_MIN) {
                        switchOff(ledTracking);
                        serial.sendMsg("r");
                        currentState = State.SCANNING;
                    } else if(prevDist != distance) {
                        prevDist = distance;
                        printAndLogTracked(angle, distance);
                    }
                }
            } else if (ev instanceof ButtonPressed){
                if (((ButtonPressed) ev).getSourceButton().equals(buttonOff)) {
                    serial.sendMsg("o");
                    switchOff(ledOn);
                    switchOff(ledTracking);
                    currentState = State.IDLE;
                }
              }
            break;
            
        }
        
    }
    
    private static void switchOn(Light light) {
        try {
            light.switchOn();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static void switchOff(Light light) {
        try {
            light.switchOff();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static void flash(Light light, long duration) {
        new Thread(() -> {
            try {
                light.switchOn();
                Thread.sleep(duration);
                light.switchOff();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
    }
    
    private void printAndLogDetected(int angle) {
        Date date = new Date();
        String printFormattedDate = PRINT_DF.format(date);
        String logFormattedDate = LOG_DF.format(date);
        System.out.println("Time " + printFormattedDate + " - Object detected at angle " + angle);
        this.logger.println("[" + logFormattedDate + "] - Object detected at angle " + angle);
    }
    
    private void printAndLogTracked(int angle, float distance) {
        Date date = new Date();
        String printFormattedDate = PRINT_DF.format(date);
        String logFormattedDate = LOG_DF.format(date);
        System.out.println("Time " + printFormattedDate + " - Object tracked at angle " + angle + " distance " + distance);
        this.logger.println("[" + logFormattedDate + "] - Object tracked at angle " + angle + " distance " + distance);
    }
    
    private void printAndLogScanComplete(int totalDetected) {
        Date date = new Date();
        String printFormattedDate = PRINT_DF.format(date);
        String logFormattedDate = LOG_DF.format(date);
        System.out.println("Time " + printFormattedDate + " - 180 deg scan complete. Total # of detected objects: " + totalDetected);
        this.logger.println("[" + logFormattedDate + "] - 180 deg scan complete. Total # of detected objects: " + totalDetected);
    }
}
