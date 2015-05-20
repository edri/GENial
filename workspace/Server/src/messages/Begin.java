package messages;

import communication.MessageHandler;


/**
 * indique au client que le jeu peut commencer ainsi que la difficulte choisie
 */
public class Begin extends Message {
	private int difficulty;
	
	public Begin(int i){
		difficulty = i;
	}
	
	public void setDifficulty(int i){
		difficulty = i;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
