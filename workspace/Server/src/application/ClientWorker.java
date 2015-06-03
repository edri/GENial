package application;
/*
 * Moret Jérôme 
 * 29 avr. 2015
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.PortableInterceptor.SUCCESSFUL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

import tools.JsonObjectMapper;
import messages.*;
import protocol.*;
import database.*;
import application.*;

/**
 *
 * @author Jérôme
 */
public class ClientWorker implements Runnable {
	private Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	private Database db;
	private int playerId;
	private ServerGame myGame;
	private String playerName = "Un joueur inconnu";
	private Server server;

	public ClientWorker(Socket socket) {
		server = Server.getInstance(); // R�cup�re l'instance du serveur
		this.socket = socket;
		try {
			db = Database.getInstance(); // R�cup�re l'instance vers la DB
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PrintWriter getOutputStream() {
        return writer;
	}

	// Envois un message de succ�s au client
	private void success() {
		writer.println(Protocol.CMD_ACCEPT);
		System.out.println("Serveur retourne : SUCCESS");
	}
	
	// Envois un message de refus au client avec un message
	private void failure(String reason) {
		writer.println(Protocol.CMD_REFUSE);
		try {
			writer.println(JsonObjectMapper.toJson(new Refuse(reason)));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Serveur retourne : REFUSE, " + reason);
	}
	

	public String getPlayerName() {
		return playerName;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public void setMyGame(ServerGame myGame) {
		this.myGame = myGame;
	}
	
	@Override
	public void run() {
		// Initialisation des fluxs
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException ex) {
			Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
		}

		String command;
		boolean exit = false;
		try {
			while (!exit && ((command = reader.readLine()) != null)) {
				System.out.println("Serveur a re�u la commande : " + command);
				switch (command) {
					case Protocol.CMD_AUTH: // Je veux me connecter
						Auth authMsg = JsonObjectMapper.parseJson(reader.readLine(), Auth.class);
						
						ResultSet rs = db.auth(authMsg.getName(), authMsg.getPwd());
						if(rs.next()) {
							success();
							playerId = rs.getInt(1);
							playerName = rs.getString(2);
							writer.println(Protocol.CMD_GAMES_LIST);
							writer.println(JsonObjectMapper.toJson(new GamesList(server.getLobbies())));
						} else {
							failure("Nom de joueur ou mot de passe incorrect.");
						}
						
						break;
						
					case Protocol.CMD_REGISTER: // Je veux m'enregistrer
						String line = reader.readLine();
						Register reg = JsonObjectMapper.parseJson(line, Register.class);
						
						if(db.playerAlreadyExist(reg.getName(), reg.getPwd())) {
							failure("Ce nom de joueur existe deja.");
						} else {
							db.createPlayer(reg.getName(), reg.getPwd());
							success();
							writer.println(Protocol.CMD_GAMES_LIST);
							writer.println(JsonObjectMapper.toJson(new GamesList(server.getLobbies())));
						}
						break;
					
					case Protocol.CMD_CREATE: // Je veux cr�er une partie
						Create createGame = JsonObjectMapper.parseJson(reader.readLine(), Create.class);
						if(server.getGames().size() == 1) { // Pour l'instant, on autorise un seul jeu
							failure("Un jeu a deja ete cree.");
						} else {
							ServerGame tmp = new ServerGame(createGame.getNbCases(), createGame.getDifficulty(), 
									new ArrayList<>(Arrays.asList(this)), createGame.getName(), createGame.getNbPlayers());
							myGame = tmp;
							server.addGame(tmp);
							success();
						}
						
						break;
					
					case Protocol.CMD_JOIN: // Je veux rejoindre une partie
						Join joinGame = JsonObjectMapper.parseJson(reader.readLine(), Join.class);
						if(server.getGames().get(joinGame.getGameName()) == null) {
							failure("Cette partie n'existe pas !");
							break;
						}
						
						if(!server.getGames().get(joinGame.getGameName()).isComplete()) { // La partie a encore de la place
							server.getGames().get(joinGame.getGameName()).addPlayer(playerName, this);
							success();
						} else {
							failure("Il n'y a plus de place dans cette partie.");
						}
						break;
						
					case Protocol.CMD_REFRESH: // Je veux raffraichir
						writer.println(Protocol.CMD_GAMES_LIST);
						writer.println(JsonObjectMapper.toJson(new GamesList(server.getLobbies())));
						break;
						
                    case Protocol.CMD_START: // Je d�marre la partie
                    	// D�marre le thread de la partie en cours (un ServerGame)
                        new Thread(myGame).start();
                        break;
                        
                    case Protocol.CMD_ROLL: // J'ai lanc� les d�s
                    	writer.println(Protocol.CMD_MVT);
                    	int movement = new Random().nextInt(6) + 1;
                    	writer.println(JsonObjectMapper.toJson(new Mvt(movement)));
                    	synchronized(myGame) {
                    		myGame.movePlayer(playerName, movement);
                    		myGame.notify();
                    	}
                    	break;
						
                    case Protocol.CMD_CHOOSE_GAME: // J'ai choisi un mini-jeu
                    	//ChooseGame gameChosen = JsonObjectMapper.parseJson(reader.readLine(), ChooseGame.class);
                    	synchronized(myGame) {
                    		myGame.notify();
                    	}
                    	break;
                    case Protocol.SEND_RESULT: // J'ai fini de jouer, je te transmets mon score
                    	SendResult results = JsonObjectMapper.parseJson(reader.readLine(), SendResult.class);
                    	myGame.addScores(playerName, results.getScore());
                    	break;
					case Protocol.CMD_QUIT: // Je quitte la partie
						exit = true;
						break;
						
					default:
						System.out.println("Commande non-reconnue.");
						break;

				}
			}
		} catch (IOException e) {
			// Un client se d�connecte
			System.out.println(playerName + " a �t� d�connect� !");
			try {
				socket.close();
				server.disconnectPlayer(this);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
