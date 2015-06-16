package application;
import gui.GameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import communication.MessageReader;

public class Game extends Observable implements Runnable {
	private boolean isCreator;
	private int nbCases;
	private int difficulty;
	private String winner;
	private ArrayList<String> players = new ArrayList<>();
	private Map<String, Integer> positions = new HashMap<>();
	private String name;
	private String playerTurn;
	private int maxPlayers;
	private MessageReader msgReader;
	private boolean finished;

	// pas utilise
	public Game() {
	}

	/**
	 * Constructeur permettant de créer une partie (plateau de jeu), une partie est crée
	 * uniquement lorsque le jeu se lance (cf classe Lobby pour avant le lancement)
	 * @param nbCases
	 * @param difficulty
	 * @param players
	 * @param name
	 * @param maxPlayers
	 */
	public Game(int nbCases, int difficulty, ArrayList<String> players, String name, int maxPlayers, MessageReader msgReader) {
		this.nbCases = nbCases;
		this.players = players;
		this.difficulty = difficulty;
		this.name = name;
		this.maxPlayers = maxPlayers;
		this.msgReader = msgReader;
		isCreator = false;
		finished = false;

		// positionne tous les joueurs à la position 0 (départ)
		for (String player : players)
		{
			positions.put(player, 0);
		}
	}

	/**
	 * Retourne le nom de la partie
	 * @return name, nom de la partie
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retourne le nombre max de joueur (A DEPLACER DANS LOBBY)
	 * @return
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * Retourne le nombre de cases du plateau de jeu
	 * @return nbCases
	 */
	public int getNbCases() {
		return nbCases;
	}

	/**
	 * Assigne le nombre de cases fourni au plateau
	 * @param nbCases, le nombre de cases voulues
	 */
	public void setNbCases(int nbCases) {
		this.nbCases = nbCases;
		setChanged();
		notifyObservers();
	}

	/**
	 * Retourne la difficulté de la partie
	 * @return difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * Assigne une difficulté au jeu (utilisée lors du lancement des mini-jeux)
	 * @param difficulty
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		setChanged();
		notifyObservers();
	}

	/**
	 * Retourne la liste des joueurs participants à la partie
	 * @return
	 */
	public ArrayList<String> getPlayers() {
		return players;
	}

	/**
	 * Assigne une liste de joueurs à la partie (normalement pas utilisé)
	 * @param players
	 */
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
		setChanged();
		notifyObservers();
	}

	/**
	 * Action - methode appelée par l'application afin de déplacer le joueur
	 * dont c'est le tour
	 * @param moveValue
	 * @return
	 */
	public boolean movePlayer(int moveValue)
	{
		int newPosition = positions.get(playerTurn) + moveValue;
		if (newPosition >= nbCases)
		{
			positions.replace(playerTurn, nbCases);
			winner = playerTurn;
			setChanged();
			notifyObservers();
			return true;
		}
		else
		{
			positions.replace(playerTurn, newPosition);
			setChanged();
			notifyObservers();
			return false;
		}
	}

	/**
	 * Retourne le nom du joueur ayant gagné la partie (plateau)
	 * @return
	 */
	public String getWinner()
	{
		return winner;
	}

	// fonction utilisée dans les tests
	public void showStatus()
	{
		System.out.println("Statut de la partie : ");

		for (String player : players)
		{
			System.out.println("\t" + player + " : " + positions.get(player) + ".");
		}
	}

	/**
	 * Action - Ajoute un nouveau joueur à la partie (normalement ne sera plus utilisé)
	 * @param name
	 */
	public void addPlayer(String name) {
		// on ajoute le joueur a la liste de joueur
		players.add(name);
		// on lie le nouveau joueur a sa position de depart
		positions.put(name, 0);
		setChanged();
		notifyObservers();
	}

	/**
	 * Action - Retire un joueur de la partie (généralement suite à une déconnexion)
	 * @param name
	 */
	public void removePlayer(String name) {
		for(int i = 0; i < players.size(); i++){
			if (players.get(i).equals(name)){
				// on supprime le joueur de la liste de joueur
				players.remove(i);
				// on supprime la position du joueur
				positions.remove(name);
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Action - Indique le nom du joueur dont c'est le tour
	 * @param name
	 */
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
		setChanged();
		notifyObservers();
	}

	/**
	 * Action - Assigne au plateau le nom du joueur ayant gagné la partie (plateau)
	 * @param winner
	 */
	public void setWinner(String winner) {
		this.winner = winner;
	}

	public  Map<String, Integer> getPositions() {
		return positions;
	}

	public boolean isCreator(){
		return isCreator;
	}

	public void setCreator(boolean isCreator){
		this.isCreator = isCreator;
	}
	
	public String getPlayerTurn(){
		return playerTurn;
	}
	
	public void setFinished(boolean b){
		finished = b;
	}
	
	@Override
	public void run() {
		while(!finished){
			msgReader.getMessage();
		}
	}
}
