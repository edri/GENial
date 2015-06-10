package application;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;

import database.Database;
import messages.Begin;
import messages.Dice;
import messages.SelectGame;
import messages.StartGame;
import messages.Winner;
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
	private Iterator<String> currentPlayer;
	private Iterator<String> currentGameChooser;
	//private int currentGameChooser = 0;
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
	
	public void sendToOne(ClientWorker who, String... msgs) {
		for(String msg : msgs)
			who.getOutputStream().println(msg);
	}
	
	public void sendToAll(String... msgs) {
		for(ClientWorker player : connections.values()) {
			sendToOne(player, msgs);
		}
	}

	public void movePlayer(String player, int movement) {
		int newPosition = positions.get(player) + movement;
		
		if(newPosition >= nbCases) {
			try {
				sendToAll(Protocol.CMD_WINNER, JsonObjectMapper.toJson(new Winner(player)));
				Thread.currentThread().interrupt();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			positions.replace(player, positions.get(player) + movement);
		}
	}
	
	@Override
	public void run() {
		// Envoi à chaque client un signal de commencement.
		for (ClientWorker client : connections.values()) {
			client.setMyGame(this);
            try {
            	sendToOne(client, Protocol.CMD_BEGIN, JsonObjectMapper.toJson(new Begin(difficulty)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
        }
		
		currentGameChooser = connections.keySet().iterator();
		String gameChooserName;
		
		// Boucle de jeu, lorsqu'une personne gagne, le thread de jeu est stoppé
		while(true) {
			
			if(currentGameChooser.hasNext()) {
				gameChooserName = currentGameChooser.next();
			} else {
				currentGameChooser = connections.keySet().iterator();
				gameChooserName = currentGameChooser.next();
				
			}
			
			// === Chaque joueur joue une fois le dé ===
			currentPlayer = connections.keySet().iterator();
			
			for(int i = 0; i < connections.size(); ++i) { 
				
				// Envoi à chaque client qui doit jouer le dé
				try {
					sendToAll(Protocol.CMD_DICE, JsonObjectMapper.toJson(new Dice(currentPlayer.next())));
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				synchronized(this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				
				if(Thread.currentThread().isInterrupted())
					break;
					
			} 
			// =======
			
			if(Thread.currentThread().isInterrupted())
				break;
			
			// Envoi du signal de selection du mini-jeu
			try {
				sendToOne(connections.get(gameChooserName), Protocol.CMD_SELECT_GAME, JsonObjectMapper.toJson(new SelectGame(db.getGamesMap())));
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			//while(true);
			synchronized(this) {
				try {
					System.out.println("Before wait");
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("Sorti du wait");
			
			startMiniGame(); // Lancement du mini-jeu
			while(scores.size() != connections.values().size()); // Tant qu'on a pas reçu tous les scores
			defineWinner(); // Définition du vainqueur du mini-jeu
		}	
		
	}
	
	private void defineWinner() {
		Iterator<String> iterPlayers = connections.keySet().iterator();
		String winnerName = iterPlayers.next(); // Vainqueur = temporairement 1er
		String playerNameTmp;
		
		// Parcours des autres et remplacement si il y a un meilleur
		for(int i = 1; i < connections.values().size(); ++i) {
			playerNameTmp = iterPlayers.next();
			if(scores.get(playerNameTmp) > scores.get(winnerName))
				winnerName = playerNameTmp;
		}
		
		// Envoi à tous les joueurs le vainqueur du mini-jeu
		try {
			sendToAll(Protocol.CMD_WINNER_GAME, JsonObjectMapper.toJson(new WinnerGame(winnerName, scores.get(winnerName))));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		int seed = new Random().nextInt();
		try {
			System.out.println("Start game envoye");
			sendToAll(Protocol.CMD_START_GAME, JsonObjectMapper.toJson(new StartGame(miniGameId, seed)));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, ClientWorker> getConnections() {
		return connections;
	}
	
	public boolean isComplete() {
		return connections.size() >= maxPlayers;
	}
	
	public void disconnectPlayer(String name) {
		if(connections.remove(name) != null) {
			System.out.println("Y a un blaireau qui s'est déconnecté de la partie ...");
			if(connections.isEmpty()) {
				Server.getInstance().deleteGame(this);
				Thread.currentThread().interrupt();
			}
		}
	}
}
