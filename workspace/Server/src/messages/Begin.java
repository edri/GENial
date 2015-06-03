package messages;




/**
 * indique au client que le jeu peut commencer ainsi que la difficulte choisie
 */
public class Begin  {
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

}