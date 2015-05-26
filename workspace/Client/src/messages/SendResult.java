package messages;

import communication.MessageHandler;

/**
 * Indique au serveur le score obtenu pour le mini-jeu courant
 */
public class SendResult extends Message {
	private int score;
	
	public SendResult(){
		score = -1;
	}
	
	public SendResult(int score){
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
