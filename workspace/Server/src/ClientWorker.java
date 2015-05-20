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
import java.util.logging.Level;
import java.util.logging.Logger;

import tools.JsonObjectMapper;
import messages.*;
import protocol.*;
/**
 *
 * @author JÃ©rÃ´me
 */
public class ClientWorker implements Runnable {
	private Socket socket;
	BufferedReader reader;
	PrintWriter writer;

	public ClientWorker(Socket socket) {
		this.socket = socket;
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
						AUTH authMsg = JsonObjectMapper.parseJson(reader.readLine(), AUTH.class);
						
						if(true) {
							writer.println(Protocol.CMD_ACCEPT);
						} else {
							writer.println(Protocol.CMD_REFUSE);
						}
						
						break;
						
					case Protocol.CMD_REGISTER:
						System.out.println("Debut de register");
						String line = reader.readLine();
						System.out.println("Recu le json : "  + line);
						REGISTER reg = JsonObjectMapper.parseJson(line, REGISTER.class);
						System.out.println("Fin de register : " + reg.getName() + ", " + reg.getPwd());
						
						if(true) {
							System.out.println("Je vais écrire " + Protocol.CMD_ACCEPT);
							writer.println(Protocol.CMD_ACCEPT);
							System.out.println("J'ai fini.");
						} else {
							writer.println(Protocol.CMD_REFUSE);
						}
						break;
					
					case Protocol.CMD_CREATE:
						CREATE createGame = JsonObjectMapper.parseJson(reader.readLine(), CREATE.class);
						
						if(true) {
							writer.println(Protocol.CMD_ACCEPT);
						} else {
							writer.println(Protocol.CMD_REFUSE);
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
			// Un client se déconnecte
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
