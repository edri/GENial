package application;
/*
 * Moret JÃ©rÃ´me 
 * 29 avr. 2015
 * 
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import database.Database;

/**
 *
 * @author JÃ©rÃ´me
 */
public class Server {

	private int port;
	private boolean shouldRun;
	private ServerSocket serverSocket;
	private Thread serverThread;
	private Map<String, ServerGame> games = new HashMap<>();

	// Liste des clients connectés
	private ArrayList<ClientWorker> clients = new ArrayList<>();

	public static final int PORT = 7777;

	// Instance unique du serveur
	private static Server instance;

	// Récupérer l'instance unique du serveur
	public static Server getInstance() {
		if (instance == null) {
			instance = new Server(PORT);
		}

		return instance;
	}

	private Server(int port) {
		this.port = port;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				shouldRun = true;
				while (shouldRun) {
					try {
						System.out.println("Attente de client sur "
								+ serverSocket.getLocalSocketAddress());
						Socket clientSocket = serverSocket.accept();
						System.out.println("Un nouveau client est arrivé ...");
						ClientWorker worker = new ClientWorker(clientSocket);
						System.out
								.println("Délegation du travail à un client worker...");
						Thread clientThread = new Thread(worker);

						// Ajouter le client à la liste des clients connectés
						clients.add(worker);

						clientThread.start();
					} catch (IOException ex) {
						System.out
								.println("IOException in main server thread, exit: "
										+ ex.getMessage());
						shouldRun = false;
					}
				}
			}
		});
		serverThread.start();
	}

	public void stopServer() throws IOException {
		shouldRun = false;
		serverSocket.close();
	}

	public ArrayList<ClientWorker> getClients() {
		return clients;
	}

	public static void main(String[] args) {
		// Lance le serveur
		getInstance();
	}
	
	public Map<String, ServerGame> getGames() {
		return games;
	}
	
	public void addGame(ServerGame game) {
		games.put(game.getName(), game);
	}

}
