package messages;

import communication.MessageHandler;


/**
 * Indique au serveur que l'on souhaite rejoindre la partie ayant ce nom
 */
public class Join extends Message {
	private String gameName;
	
	public Join(String name){
		gameName = name;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
