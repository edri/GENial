package messages;

/**
 * Indique le nom du joueur ayant gagne la partie 
 */
public class WINNER {
	private String winnerName;
	
	public WINNER(String name){
		winnerName = name;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}
}
