package messages;

import communication.MessageHandler;

/**
 * Indique aux joueurs qui doit lancer son des
 */

public class Dice extends Message {
	private String playerName;
	
	public Dice(String name){
		playerName = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
