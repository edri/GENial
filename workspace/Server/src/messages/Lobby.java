package messages;

import java.util.ArrayList;

public class Lobby {
	private int nbSquares;
	private ArrayList<String> players;
	int difficulty;
	String name;
	int maxPlayers;
	
	

	public Lobby(int nbSquares, ArrayList<String> players, int difficulty,
			String name, int maxPlayers) {
		this.nbSquares = nbSquares;
		this.players = players;
		this.difficulty = difficulty;
		this.name = name;
		this.maxPlayers = maxPlayers;
	}

	public int getNbSquares() {
		return nbSquares;
	}

	public void setNbSquares(int nbSquares) {
		this.nbSquares = nbSquares;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	
}
