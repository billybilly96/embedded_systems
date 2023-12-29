package main;

import application.Gui;
import javafx.application.Application;
import serial_monitor.CommChannel;
import serial_monitor.SerialCommChannel;

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
        System.out.println("Start monitoring serial port "+args[0]+" at baud rate: "+args[1]);
//        new Gui(new SerialCommChannel(comPortName, dataRate));
        Application.launch(Gui.class, args);
    }
    
    public static CommChannel getCommChannel() {
        try {
            return new SerialCommChannel(commPort, dataRate);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
