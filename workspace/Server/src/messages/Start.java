package messages;

import communication.MessageHandler;

/**
 * Indique au serveur que l'administrateur de la partie veut la debuter
 */
public class Start extends Message {
	public Start() { }
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}