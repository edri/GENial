package messages;

import communication.MessageHandler;

/**
 * Indique la deconnexion d'un joueur de la partie
 */
public class Disconnect extends Message {
	private String leaverName;
	
	public Disconnect(String name){
		leaverName = name;
	}

	public String getLeaverName() {
		return leaverName;
	}

	public void setLeaverName(String leaverName) {
		this.leaverName = leaverName;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}