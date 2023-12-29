package radar.raspi.main;

import radar.raspi.common.Msg;

public class PositionInfoMsg implements Msg {
    private int angle;
    private float distance;
    
    public PositionInfoMsg(final int angle, final float distance) {
        this.angle = angle;
        this.distance = distance;
    }

    public int getAngle() {
        return angle;
    }

    public float getDistance() {
        return distance;
    }
    
    
}
