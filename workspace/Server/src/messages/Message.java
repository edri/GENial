package messages;

import communication.MessageHandler;

public abstract class Message {
	public abstract void accept(MessageHandler visitor);
}
