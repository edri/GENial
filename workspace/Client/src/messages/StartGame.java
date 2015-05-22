package messages;

import communication.MessageHandler;

/**
 * Indique aux joueurs que le mini-jeu peut commencer
 */
public class StartGame extends Message {
	private int gameId;
	private int seed;
	
	public StartGame(){
		gameId = -1;
		seed = -1;
	}
	
	public StartGame(int gameId, int seed){
		this.gameId = gameId;
		this.seed = seed;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
