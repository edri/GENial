package messages;

import java.util.List;

import application.Game;
import communication.MessageHandler;

/**
 * Indique au joueur la liste des parties disponibles
 */
public class GamesList extends Message {
	private List<Game> games;
	
	public GamesList(List<Game> games){
		this.games = games;
	}
	
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
