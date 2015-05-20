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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

import tools.JsonObjectMapper;
import messages.*;
import protocol.*;
import database.*;

/**
 *
 * @author Jérôme
 */
public class ClientWorker implements Runnable {
	private Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	private Database db;
	//private static int counterId;
	private int playerId;
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
				System.out.println("Serveur a re�u la commande : " + command);
				switch (command) {
					case Protocol.CMD_AUTH:
						Auth authMsg = JsonObjectMapper.parseJson(reader.readLine(), Auth.class);
						
						ResultSet rs = db.auth(authMsg.getName(), authMsg.getPwd());
						if(rs.next()) {
							writer.println(Protocol.CMD_ACCEPT);
							playerId = rs.getInt(1);
							playerName = rs.getString(2);
						} else {
							writer.println(Protocol.CMD_REFUSE);
							writer.println(JsonObjectMapper.toJson(new Refuse("Nom de joueur ou mot de passe incorrect.")));
						}
						
						break;
						
					case Protocol.CMD_REGISTER:
						System.out.println("Debut de register");
						String line = reader.readLine();
						System.out.println("Recu le json : "  + line);
						Register reg = JsonObjectMapper.parseJson(line, Register.class);
						System.out.println("Fin de register : " + reg.getName() + ", " + reg.getPwd());
						
						if(db.playerAlreadyExist(reg.getName(), reg.getPwd())) {
							writer.println(Protocol.CMD_REFUSE);
							writer.println(JsonObjectMapper.toJson(new Refuse("Ce nom de joueur existe deja.")));
						} else {
							db.createPlayer(reg.getName(), reg.getPwd());
							writer.println(Protocol.CMD_ACCEPT);
						}
						break;
					
					case Protocol.CMD_CREATE:
						Create createGame = JsonObjectMapper.parseJson(reader.readLine(), Create.class);
						
						if(db.gameAlreadyExist()) {
							writer.println(Protocol.CMD_REFUSE);
							writer.println(JsonObjectMapper.toJson(new Refuse("Un jeu a deja ete cree.")));
						} else {
							db.createGame(createGame.getName(), createGame.getNbPlayers(), createGame.getDifficulty(), createGame.getNbCases());
						}
						
						break;
						
					case Protocol.CMD_REFRESH:
						
						if(true) {
							writer.println(Protocol.CMD_ACCEPT);
						} else {
							writer.println(Protocol.CMD_REFUSE);
						}
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
			e.printStackTrace();
			// Un client se d�connecte
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
