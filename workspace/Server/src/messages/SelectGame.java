package messages;

import java.util.Map;

import communication.MessageHandler;

/**
 * Fournit au joueur la liste des jeux disponibles
 */
public class SelectGame extends Message {
	private Map<Integer, String> gamesMap;
	
	public SelectGame() {
		
	}
	
	public SelectGame(Map<Integer, String> map){
		gamesMap = map;
	}

	public Map<Integer, String> getGamesMap() {
		return gamesMap;
	}

	public void setGamesMap(Map<Integer, String> gamesMap) {
		this.gamesMap = gamesMap;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
