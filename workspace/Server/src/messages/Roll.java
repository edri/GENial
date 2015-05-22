package messages;

import communication.MessageHandler;

/**
 * Demande au serveur de lancer les des
 */
public class Roll extends Message {
	public Roll(){ }
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}