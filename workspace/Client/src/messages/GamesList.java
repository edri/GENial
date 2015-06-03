package messages;


import java.util.ArrayList;
import java.util.List;

import application.Game;
import application.Lobby;
import communication.MessageHandler;

/**
 * Indique au joueur la liste des parties disponibles
 */
public class GamesList extends Message {
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

	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
