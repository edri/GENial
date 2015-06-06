package messages;


/**
 * Indique au serveur que l'on souhaite rejoindre la partie ayant ce nom
 */
public class Join {
	private String gameName;
	
	public Join(){
		gameName = "";
	}
	
	public Join(String name){
		gameName = name;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}