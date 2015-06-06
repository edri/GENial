package messages;



/**
 * Indique l'arrivee d'un nouveau joueur dans la partie en attente de debuter
 */
public class NewPlayer {
	private String newPlayerName;
	
	public NewPlayer(){
		newPlayerName = "";
	}
	
	public NewPlayer(String name){
		newPlayerName = name;
	}

	public String getNewPlayerName() {
		return newPlayerName;
	}

	public void setNewPlayerName(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
}
