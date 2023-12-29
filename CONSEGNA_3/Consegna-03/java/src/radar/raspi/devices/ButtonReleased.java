package radar.raspi.devices;

import radar.raspi.common.Event;

public class ButtonReleased implements Event {
	private Button source;
	
	public ButtonReleased(Button source){
		this.source = source;
	}
	
	public Button getSourceButton(){
		return source;
	}
}
