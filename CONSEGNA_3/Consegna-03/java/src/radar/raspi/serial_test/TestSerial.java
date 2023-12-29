package radar.raspi.serial_test;

import radar.raspi.common.BasicEventLoopController;
import radar.raspi.common.Event;
import radar.raspi.common.Msg;
import radar.raspi.common.MsgEvent;
import radar.raspi.main.Main;
import radar.raspi.serial.SerialCommChannel;

public class TestSerial extends BasicEventLoopController {
    
    private SerialCommChannel serial;
    
    public TestSerial() {
        this.serial = Main.getSerialCommChannel();
        serial.addObserver(this);
    }
    
    @Override
    protected void processEvent(Event ev) {
        if (ev instanceof MsgEvent){
            Msg msg = ((MsgEvent) ev).getMsg();
            if (msg instanceof TestMsg){
                String msgText = ((TestMsg) msg).getMsg();
                System.out.println("Received: " + msgText);
                serial.sendMsg("OK! Received!");
            }
        }
    }

}
