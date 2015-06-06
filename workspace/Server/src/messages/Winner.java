package messages;

/**
 * Indique le nom du joueur ayant gagne la partie 
 */
public class Winner {
	private String winnerName;
	
	public Winner(){
		winnerName = "";
	}
	
	public Winner(String name){
		winnerName = name;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}
}