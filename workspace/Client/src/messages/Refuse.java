package messages;

import communication.MessageHandler;

/**
 * Indique le refus de la demande recue
 */
public class Refuse extends Message {
	public Refuse() { }
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
