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
import java.util.logging.Level;
import java.util.logging.Logger;

import tools.JsonObjectMapper;
import messages.*;
import protocol.*;
/**
 *
 * @author Jérôme
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
		writer.println("ping");
		writer.println("");
		try {
			while (!exit && ((command = reader.readLine()) != null)) {
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
						REGISTER reg = JsonObjectMapper.parseJson(reader.readLine(), REGISTER.class);
						
						if(true) {
							writer.println(Protocol.CMD_ACCEPT);
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
			// Un client se d�connecte
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
