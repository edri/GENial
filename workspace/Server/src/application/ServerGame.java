package application;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;

import database.Database;
import messages.Begin;
import messages.Dice;
import messages.SelectGame;
import messages.StartGame;
import messages.WinnerGame;
import protocol.Protocol;
import tools.JsonObjectMapper;

public class ServerGame implements Runnable {
	private int nbCases;
	private int difficulty;
	private String winner;
	private Map<String, Integer> positions = new HashMap<>();
	//private ArrayList<ClientWorker> players = new ArrayList<>();
	private Map<String, ClientWorker> connections = new HashMap<>();
	private Map<String, Integer> scores = new HashMap<>();
	private String name;
	private String playerTurn;
	private int maxPlayers;
	private int currentPlayerIdx = 0;
	private int currentGameChooser = 0;
	private boolean terminated = false;
	private boolean playerHasRoll;
	private boolean miniGameChosed = false;
	private boolean miniGameTerminated = false;
	private int miniGameId;
	private Database db;
	
	public ServerGame() {
	}
	
	public ServerGame(int nbCases, int difficulty, ArrayList<ClientWorker> players, String name, int maxPlayers) {
		this.nbCases = nbCases;
		this.difficulty = difficulty;
		this.name = name;
		this.maxPlayers = maxPlayers;
		
		for (ClientWorker player : players) {
			positions.put(player.getPlayerName(), 0);
			connections.put(player.getPlayerName(), player);
		}
		
		try {
			db = Database.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public String getWinner()
	{
		return winner;
	}
	
	public void addPlayer(String name, ClientWorker connection) {
		connections.put(name, connection);
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

	public void movePlayer(String player, int movement) {
		int newPosition = positions.get(player) + movement;
		
		if(newPosition >= nbCases) {
			connections.get(player).getOutputStream().println(Protocol.CMD_WINNER_GAME);
			try {
				connections.get(player).getOutputStream().println(JsonObjectMapper.toJson(new WinnerGame(player, 5)));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			positions.replace(player, positions.get(player) + movement);
		}
	}
	
	private void makeTurn() {
		currentPlayerIdx = 0;
		do { 
			playerHasRoll = false;
			
			// Envoi à chaque client qui doit jouer le dé
			for(ClientWorker player : connections.values()) {
				player.getOutputStream().println(Protocol.CMD_DICE);
				try {
					player.getOutputStream().println(JsonObjectMapper.toJson(new Dice(
								new ArrayList<ClientWorker>(connections.values()).get(currentPlayerIdx).getPlayerName()
							)));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			currentPlayerIdx++;
			
			// Attente sur le joueur qui repond a l'appel du lancé de dé
			while(!playerHasRoll);
			
		} while(currentPlayerIdx != connections.values().size()); // Tant qu'on a pas fait un tour complet des joueurs
	}
	
	@Override
	public void run() {
		// Envoi à chaque client un signal de commencement.
		for (ClientWorker client : connections.values()) {
			client.setMyGame(this);
            client.getOutputStream().println(Protocol.CMD_BEGIN);
            try {
				client.getOutputStream().println(JsonObjectMapper.toJson(new Begin(difficulty)));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		// Boucle de jeu, lorsqu'une personne gagne le thread de jeu est stoppé
		while(true) {
			makeTurn();	// Faire un tour
			
			// Envoi du signal de selection du mini-jeu
			new ArrayList<ClientWorker>(connections.values()).get(currentGameChooser).getOutputStream().println(Protocol.CMD_SELECT_GAME);
			try {
				new ArrayList<ClientWorker>(connections.values()).get(currentGameChooser).getOutputStream().println(JsonObjectMapper.toJson(new SelectGame(db.getGamesMap())));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Attente du retour du message pour le mini-jeu
			while(!miniGameChosed);
			startMiniGame(); // Lancement du mini-jeu
			while(scores.size() != connections.values().size()); // Tant qu'on a pas reçu tous les scores
			defineWinner(); // Définition du vainqueur du mini-jeu
			
			currentGameChooser++;
			if(currentGameChooser >= connections.values().size())
				currentGameChooser = 0;
		}	
		
	}
	
	private void defineWinner() {
		String winnerName = new ArrayList<ClientWorker>(connections.values()).get(0).getPlayerName(); // Vainqueur = 1er
		
		// Parcours des autres et remplacement si y a un meilleur
		for(int i = 1; i < connections.values().size(); ++i) {
			if(scores.get(new ArrayList<ClientWorker>(connections.values()).get(i).getPlayerName()) > scores.get(winnerName))
				winnerName = new ArrayList<ClientWorker>(connections.values()).get(i).getPlayerName();
		}
		
		// Envoi à tous les joueurs le vainqueur
		for(ClientWorker player : connections.values()) {
			player.getOutputStream().println(Protocol.CMD_WINNER);
			try {
				player.getOutputStream().println(JsonObjectMapper.toJson(new WinnerGame(winnerName, scores.get(winnerName))));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setPlayerHasRoll(boolean playerHasRoll) {
		this.playerHasRoll = playerHasRoll;
	}
	
	public void setMiniGameChosed(boolean miniGameChosed) {
		this.miniGameChosed = miniGameChosed;
	}
	
	public void setMiniGameId(int miniGameId) {
		this.miniGameId = miniGameId;
	}
	
	public synchronized void addScores(String player, int score) {
		scores.put(player, score);
	}
	
	public void startMiniGame() {
		for(ClientWorker player : connections.values()) {
			player.getOutputStream().println(Protocol.CMD_START_GAME);
			try {
				player.getOutputStream().println(JsonObjectMapper.toJson(new StartGame(miniGameId, new Random().nextInt())));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
