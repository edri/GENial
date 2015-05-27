package messages;

import java.util.ArrayList;
import java.util.List;

import application.Game;
import communication.MessageHandler;

/**
 * Indique au joueur la liste des parties disponibles
 */
public class GamesList extends Message {
	private ArrayList<Game> games;
	
	public GamesList() {
		games = new ArrayList<Game>();
	}
	
	public GamesList(ArrayList<Game> games){
		this.games = games;
	}
	
	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
