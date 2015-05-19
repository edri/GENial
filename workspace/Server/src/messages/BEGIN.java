package messages;


/**
 * indique au client que le jeu peut commencer ainsi que la difficulte choisie
 */
public class BEGIN {
	private int difficulty;
	
	public BEGIN(int i){
		difficulty = i;
	}
	
	public void setDifficulty(int i){
		difficulty = i;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
}
