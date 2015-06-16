
/**
 * 
 * @author Mélanie
 */
package click;

public abstract class Game {
	
	private int code;
	private int difficulty;
	private int seed;

	public void startGame() {
		code = 28;
		difficulty = 3;
		seed = 28;
   	}

	public void finishGame(int score) {

	}	

}
