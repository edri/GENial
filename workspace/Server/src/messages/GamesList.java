package messages;

import java.util.ArrayList;

/**
 * Indique au joueur la liste des parties disponibles
 */
public class GamesList {
	private ArrayList<Lobby> games;
	
	public GamesList() {
		games = new ArrayList<Lobby>();
	}
	
	public GamesList(ArrayList<Lobby> games){
		this.games = games;
	}
	
	public ArrayList<Lobby> getGames() {
		return games;
	}

	public void setGames(ArrayList<Lobby> games) {
		this.games = games;
	}
}
