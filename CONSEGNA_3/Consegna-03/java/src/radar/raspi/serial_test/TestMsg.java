package radar.raspi.serial_test;

import radar.raspi.common.Msg;

public class TestMsg implements Msg{
    
    private String msg;

    public TestMsg(String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
}
