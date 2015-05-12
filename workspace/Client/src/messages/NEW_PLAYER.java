package messages;


/**
 * Indique l'arrivee d'un nouveau joueur dans la partie en attente de debuter
 */
public class NEW_PLAYER {
	private String newPlayerName;
	
	public NEW_PLAYER(String name){
		newPlayerName = name;
	}

	public String getNewPlayerName() {
		return newPlayerName;
	}

	public void setNewPlayerName(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
}
