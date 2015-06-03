package messages;

import communication.MessageHandler;

public class Accept extends Message {
	
	public Accept(){
		
	}

	@Override
	public void accept(MessageHandler visitor) {
		// handle by server
	}

}
