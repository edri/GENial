package messages;

/**
 * Indique aux joueurs qui doit lancer son des
 */

public class DICE {
	private String playerName;
	
	public DICE(String name){
		playerName = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
