package messages;

import communication.MessageHandler;

/**
 * Administrateur de la partie indique la fin de cette derniere
 * (finalement pas implemente)
 */
public class Quit extends Message {
	public Quit(){ }
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
