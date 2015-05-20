package messages;

import communication.MessageHandler;

/**
 * renseigne l'acceptation de la requ�te recue
 */
public class Accept extends Message {

	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
