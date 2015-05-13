package messages;

import java.util.Map;

/**
 * Fournit au joueur la liste des jeux disponibles
 */
public class SELECT_GAME {
	private Map<Integer, String> gamesMap;
	
	public SELECT_GAME(Map<Integer, String> map){
		gamesMap = map;
	}

	public Map<Integer, String> getGamesMap() {
		return gamesMap;
	}

	public void setGamesMap(Map<Integer, String> gamesMap) {
		this.gamesMap = gamesMap;
	}
}
