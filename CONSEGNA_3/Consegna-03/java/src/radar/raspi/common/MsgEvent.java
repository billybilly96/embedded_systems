package radar.raspi.common;

public class MsgEvent implements Event {
	
	private Msg msg;
	
	public MsgEvent(Msg msg){
		this.msg = msg;
	}
	
	public Msg getMsg(){
		return msg;
	}
	
}
