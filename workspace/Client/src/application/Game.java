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
	private String name;
	private String playerTurn;
	private int maxPlayers;
	
	public Game() {
	}
	
	public Game(int nbCases, int difficulty, ArrayList<String> players, String name, int maxPlayers) {
		this.nbCases = nbCases;
		this.players = players;
		this.difficulty = difficulty;
		this.name = name;
		this.maxPlayers = maxPlayers;
		
		for (String player : players)
		{
			positions.put(player, 0);
		}
	}
	
	public String getName() {
		return name;
	}

	public int getMaxPlayers() {
		return maxPlayers;
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

	public boolean movePlayer(int moveValue)
	{
		int newPosition = positions.get(playerTurn) + moveValue;
		
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

	public void addPlayer(String name) {
		players.add(name);
	}
	
	public void removePlayer(String name) {
		for(int i = 0; i < players.size(); i++){
			if (players.get(i).equals(name)){
				players.remove(i);
			}
		}
	}
	
	public void setPlayerTurn(String name){
		playerTurn = name;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPositions(Map<String, Integer> positions) {
		this.positions = positions;
	}
	
	public void setWinner(String winner) {
		this.winner = winner;
	}
}
