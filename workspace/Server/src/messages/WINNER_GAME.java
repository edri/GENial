package messages;

/**
 * Indique le nom et le score du joueur ayant gagne le mini-jeu
 */
public class WINNER_GAME {
	private String playerName;
	private int score;
	
	public WINNER_GAME(String name, int score){
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
