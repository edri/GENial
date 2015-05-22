package messages;

import communication.MessageHandler;

/**
 *	Message contenant un entier identifiant le jeu souhaite
 */
public class ChooseGame extends Message {
	private int gameId;
	
	public ChooseGame(int i){
		gameId = i;
	}
	
	public void setGameId(int i){
		gameId = i;
	}
	
	public int getGameId(){
		return gameId;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
