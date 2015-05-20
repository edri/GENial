package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
	private int nbCases;
	private int difficulty;
	private String winner;
	private ArrayList<String> players = new ArrayList<>();
	private Map<String, Integer> positions = new HashMap<>();
	
	public Game(int nbCases, int difficulty, ArrayList<String> players) {
		this.nbCases = nbCases;
		this.players = players;
		this.difficulty = difficulty;
		
		for (String player : players)
		{
			positions.put(player, 0);
		}
	}
	
	public int getNbCases() {
		return nbCases;
	}

	public void setNbCases(int nbCases) {
		this.nbCases = nbCases;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public boolean movePlayer(String name, int moveValue)
	{
		int newPosition = positions.get(name) + moveValue;
		
		if (newPosition >= nbCases)
		{
			positions.replace(name, nbCases);
			winner = name;
			return true;
		}
		else
		{
			positions.replace(name, newPosition);
			return false;
		}
	}
	
	public String getWinner()
	{
		return winner;
	}
	
	public void showStatus()
	{
		System.out.println("Statut de la partie : ");
		
		for (String player : players)
		{
			System.out.println("\t" + player + " : " + positions.get(player) + ".");
		}
	}
}
