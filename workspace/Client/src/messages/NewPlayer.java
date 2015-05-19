package messages;

import communication.MessageHandler;


/**
 * Indique l'arrivee d'un nouveau joueur dans la partie en attente de debuter
 */
public class NewPlayer extends Message {
	private String newPlayerName;
	
	public NewPlayer(String name){
		newPlayerName = name;
	}

	public String getNewPlayerName() {
		return newPlayerName;
	}

	public void setNewPlayerName(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
