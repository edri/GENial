package application;
/*
 * Moret JÃ©rÃ´me 
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
 * @author JÃ©rÃ´me
 */
public class ClientWorker implements Runnable {
	private Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	private Database db;
	//private static int counterId;
	private int playerId;
	private ServerGame myGame;
	private String playerName = "";

	public ClientWorker(Socket socket) {
		this.socket = socket;
		try {
			db = Database.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PrintWriter getOutputStream() {
        return writer;
	}

	private void success() {
		writer.println(Protocol.CMD_ACCEPT);
		System.out.println("Serveur retourne : SUCCESS");
	}
	
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
				System.out.println("Serveur a reçu la commande : " + command);
				switch (command) {
					case Protocol.CMD_AUTH:
						Auth authMsg = JsonObjectMapper.parseJson(reader.readLine(), Auth.class);
						
						ResultSet rs = db.auth(authMsg.getName(), authMsg.getPwd());
						if(rs.next()) {
							success();
							playerId = rs.getInt(1);
							playerName = rs.getString(2);
							writer.println(Protocol.CMD_GAMES_LIST);
							writer.println(JsonObjectMapper.toJson(new GamesList(db.fetchGamesList())));
						} else {
							failure("Nom de joueur ou mot de passe incorrect.");
						}
						
						break;
						
					case Protocol.CMD_REGISTER:
						String line = reader.readLine();
						Register reg = JsonObjectMapper.parseJson(line, Register.class);
						
						if(db.playerAlreadyExist(reg.getName(), reg.getPwd())) {
							failure("Ce nom de joueur existe deja.");
						} else {
							db.createPlayer(reg.getName(), reg.getPwd());
							success();
							writer.println(Protocol.CMD_GAMES_LIST);
							writer.println(JsonObjectMapper.toJson(new GamesList(db.fetchGamesList())));
						}
						break;
					
					case Protocol.CMD_CREATE:
						Create createGame = JsonObjectMapper.parseJson(reader.readLine(), Create.class);
						if(db.gameAlreadyExist()) {
							failure("Un jeu a deja ete cree.");
						} else {
							db.createGame(playerId, createGame.getName(), createGame.getNbPlayers(), createGame.getDifficulty(), createGame.getNbCases());
							myGame = new ServerGame(createGame.getNbCases(), createGame.getDifficulty(), new ArrayList<>(Arrays.asList(this)), createGame.getName(), createGame.getNbPlayers());
							Server.getInstance().addGame(myGame);
							success();
						}
						
						break;
					
					case Protocol.CMD_JOIN:
						Join joinGame = JsonObjectMapper.parseJson(reader.readLine(), Join.class);
						if(db.addPlayerToGame(playerId, joinGame.getGameName())) {
							Server.getInstance().getGames().get(joinGame.getGameName()).addPlayer(playerName, this);
							success();
						} else {
							failure("Il n'y a plus de place dans cette partie.");
						}
						break;
						
					case Protocol.CMD_REFRESH:
						writer.println(Protocol.CMD_GAMES_LIST);
						writer.println(JsonObjectMapper.toJson(new GamesList(db.fetchGamesList())));
						break;
						
                    case Protocol.CMD_START:
                    	// Démarre le thread de la partie en cours (un ServerGame)
                        new Thread(myGame).start();
                        break;
                        
                    case Protocol.CMD_ROLL:
                    	writer.println(Protocol.CMD_MVT);
                    	int movement = new Random().nextInt(6) + 1;
                    	writer.println(JsonObjectMapper.toJson(new Mvt(movement)));
                    	myGame.movePlayer(playerName, movement);
                    	myGame.setPlayerHasRoll(true);
                    	break;
						
                    case Protocol.CMD_CHOOSE_GAME:
                    	ChooseGame gameChosen = JsonObjectMapper.parseJson(reader.readLine(), ChooseGame.class);
                    	myGame.setMiniGameId(gameChosen.getGameId());
                    	myGame.setMiniGameChosed(true);
                    	break;
                    case Protocol.SEND_RESULT:
                    	SendResult results = JsonObjectMapper.parseJson(reader.readLine(), SendResult.class);
                    	myGame.addScores(playerName, results.getScore());
                    	break;
					case Protocol.CMD_QUIT:
						exit = true;
						break;
						
					default:
						System.out.println("Commande non-reconnue.");
						break;

				}
			}
		} catch (IOException e) {
			// Un client se déconnecte
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
