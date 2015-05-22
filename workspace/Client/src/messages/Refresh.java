package messages;

import communication.MessageHandler;

/**
 * Demande au serveur la liste des parties disponibles
 */
public class Refresh extends Message {
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}