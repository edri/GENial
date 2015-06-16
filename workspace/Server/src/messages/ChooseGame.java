package messages;

/**
 *	Message contenant un entier identifiant le jeu souhaite
 */
public class ChooseGame {
	private int gameId;
	
	public ChooseGame(){
		gameId = -1;
	}
	
	public ChooseGame(int i){
		gameId = i;
	}
	
	public void setGameId(int i){
		gameId = i;
	}
	
	public int getGameId(){
		return gameId;
	}
}
