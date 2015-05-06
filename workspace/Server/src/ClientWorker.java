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

import protocol.*;
import tools.JsonObjectMapper;

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

		while (true) {
			try {
				switch (reader.readLine()) {
					case Protocol.CMD_AUTH:
						AuthMessage authMsg = JsonObjectMapper.parseJson(reader.readLine(), AuthMessage.class);
						System.out.println("Login: " + authMsg.getPseudo());
						System.out.println("Password: " + authMsg.getHashPassword());
						break;
					default:
						System.out.println("Commande non-reconnue.");
						break;

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
