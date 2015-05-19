package messages;

/**
 * Indique au serveur le score obtenu pour le mini-jeu courant
 */
public class SEND_RESULT {
	private int score;
	
	public SEND_RESULT(int score){
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
