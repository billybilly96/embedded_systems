package application;
	
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import serial_monitor.CommChannel;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class Gui extends Application{
    
    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_X_1 = 50;
    public static final int BUTTON_Y_1 = 150;
    public static final int BUTTON_X_2 = -50;
    public static final int BUTTON_Y_2 = 200;
    public static final int BUTTON_X_3 = -150;
    public static final int BUTTON_Y_3 = 250;
    public static final int SCENE_DIM_X = 500;
    public static final int SCENE_DIM_Y = 400;
    public static final int TEXT_AREA_X = 300;
    public static final int TEXT_AREA_Y = 200;
//    public static final String PORT = "COM9";
//    public static final int RATE = 9600;
    private static final int DOOR_CLOSING_DELAY = 2;
    
    private TextArea textArea;
    private Button button1;
    private Button button2;
    private Button button3;
    private HBox hbox;
    private CommChannel monitor;
    private Thread parking;
//    private Stage primaryStage;
    
    public Gui() {
        this.monitor = Main.getCommChannel();
    }
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;
        textArea = new TextArea();
        button1 = new Button("ARRIVING");
        button2 = new Button("STOP");
        button3 = new Button("Clear");
        hbox = new HBox(textArea, button1, button2, button3);
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);
        primaryStage.setTitle("Smart Garage");
        primaryStage.setResizable(false);   
        setButtonAction();
        setPosition();
        setDimension();
        setStyle();
        this.textArea.setText("Waiting for Arduino reboot...");
        final PauseTransition ardRebPause = new PauseTransition(Duration.seconds(4));
        final PauseTransition pirCalibPause = new PauseTransition(Duration.seconds(10));
        ardRebPause.setOnFinished(e -> {
            this.textArea.setText("Calibrating sensor...");
            pirCalibPause.playFromStart();
        });
        pirCalibPause.setOnFinished(e -> {
            this.textArea.setText(".::Car OnBoard PC::.\n");
            button1.setDisable(false);
            button3.setDisable(false);
        });
        this.textArea.setEditable(false);
        Scene scene = new Scene(hbox, SCENE_DIM_X, SCENE_DIM_Y);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
        ardRebPause.playFromStart();
    }
        
   private void setButtonAction() {
       button1.setOnAction(action -> {
           this.monitor.getQueue().clear();
           this.monitor.sendMsg("a");
           String received = null;
           try {
               received = monitor.receiveMsg();
               System.out.println(received);
           } catch (InterruptedException e1) {
               e1.printStackTrace();
           }
           if (received.equals("Welcome Home.")) {
               this.textArea.appendText(received + "\n");
               parking = new Thread(new ParkingAssistance(this.monitor, this));
               parking.start();
               this.button1.setDisable(true);
               this.button2.setDisable(false);
           }
       });
       button2.setOnAction(action -> this.monitor.sendMsg("s"));
       button3.setOnAction(action -> {
           textArea.setText("");       
       });
   }
    
   private void setPosition() {
       button1.setTranslateX(BUTTON_X_1);
       button1.setTranslateY(BUTTON_Y_1);
       button2.setTranslateX(BUTTON_X_2);
       button2.setTranslateY(BUTTON_Y_2);
       button3.setTranslateX(BUTTON_X_3);
       button3.setTranslateY(BUTTON_Y_3);
   }
   
   private void setDimension() {
       textArea.setMinSize(TEXT_AREA_X, TEXT_AREA_Y);
       button1.setMinWidth(BUTTON_WIDTH);
       button2.setMinWidth(BUTTON_WIDTH);     
       button3.setMinWidth(BUTTON_WIDTH);
   }
    
   private void setStyle() {
       textArea.setStyle("-fx-background-color: red");
       button1.setStyle("-fx-border-color: red");
       button2.setStyle("-fx-border-color: red");
       button3.setStyle("-fx-border-color: red");
   }
    
    public void appendText(String text) {
        this.textArea.appendText(text + "\n");
    }
    
    public void setArrivingEnabled(boolean value) {
        this.button1.setDisable(value);
    }
    
    public void setStopEnabled(boolean value) {
        this.button2.setDisable(value);
    }
    
    public void resetScene() {
//        this.button2.setDisable(true);
//        textArea = new TextArea();
//        button1 = new Button("ARRIVING");
//        button2 = new Button("STOP");
//        button3 = new Button("Clear");
//        hbox = new HBox(textArea, button1, button2, button3);
//        setButtonAction();
//        setPosition();
//        setDimension();
//        setStyle();
//        Scene scene = new Scene(hbox, SCENE_DIM_X, SCENE_DIM_Y);
//        this.textArea.setEditable(false);
//        this.textArea.setText(".::Car OnBoard PC::.\n");
//        button2.setDisable(true);
//        final PauseTransition doorClosingTrans = new PauseTransition(Duration.seconds(2));
//        doorClosingTrans.playFromStart();
//        doorClosingTrans.setOnFinished(e -> {
//            primaryStage.setScene(scene);
//        });
        this.button2.setDisable(true);
        final PauseTransition doorClosingTrans = new PauseTransition(Duration.seconds(DOOR_CLOSING_DELAY));
          doorClosingTrans.playFromStart();
          doorClosingTrans.setOnFinished(e -> {
              this.button1.setDisable(false);
              this.textArea.setText(".::Car OnBoard PC::.\n");
          });
//        this.textArea.setText(".::Car OnBoard PC::.\n");
    }
}
