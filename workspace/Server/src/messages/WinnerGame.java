package messages;

/**
 * Indique le nom et le score du joueur ayant gagne le mini-jeu
 */
public class WinnerGame {
	private String playerName;
	private int score;
	
	public WinnerGame(){
		playerName = "";
		score = 0;
	}
	
	public WinnerGame(String name, int score){
		playerName = name;
		this.score = score;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
