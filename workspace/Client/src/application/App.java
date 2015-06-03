package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import messages.*;
import communication.*;
import gui.*;

public class App implements Runnable{
	private Game currentGame;
	private List<Game> games;
	private Scanner scan;
	private boolean start;
	private boolean endGame;
	private boolean success;
	private MessageHandler msgHandler;
	private MessageReader msgReader;
	private String clientName;
	private String status;
	
	private JFrame mainFrame;

	public App(){
		status = "connection"; // TODO utile ? A voir plus tard
		start = false;
		endGame = false;
		currentGame = null;
		clientName = "";
		games = new ArrayList<Game>();
		msgHandler = new MessageHandler(this);
		msgReader = new MessageReader(this);
		scan = new Scanner(System.in);
		
		
		ConnectionFrame temp = new ConnectionFrame(this, "GENial, connection au serveur", "Adresse IP : ", "Port : ", false);
		temp.display("Veuillez entrer l'adresse IP du serveur ainsi que le port sur lequel vous voulez vous connecter.", Color.BLACK);
		//temp = new ConnectionFrame(this, "Genial, s'identifier ou s'enregistrer");
		//temp.display("Voulez-vous vous identifier ou enregistrer un nouveau compte ?", Color.BLACK);
		mainFrame = temp;
		mainFrame.setVisible(true);
		
		
		// Vue plateau
		ArrayList<String> players = new ArrayList<>();
		players.add("Miguel");
		players.add("Jerôme");
		players.add("Mélanie");
		players.add("David");
		Game game = new Game(10, 3, players, "Partie 1", 20);
		
		GameView gameView = new GameView(game);
		gameView.setSize(new Dimension(1000, 300));
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					game.setPlayerTurn("Miguel");
					game.movePlayer(1);
					try {
					Thread.sleep(1000);
					} catch (InterruptedException e) { }
				}
			}
		}).start();
	}

	@Override
	public void run() {
		/*
		int choix;
		String pwd;
		String addrIP;
		Connection connection;

		System.out.println("Bonjour, bienvenue sur cette super application!");

		// demande de l'adresse IP du serveur et connexion
		System.out.println("Veuillez entrer l'adresse IP du serveur [format : xxx.xxx.xxx.xxx]");
		addrIP = scan.nextLine();
		connection = Connection.getInstance();
		// connection au serveur
		if (connection.connect(addrIP)){
			msgReader.setStream(Connection.getInstance().getInputStream());

			// utilisateur doit s'identifier ou creer un nouveau compte
			choix = -1;
			while(choix < 0 || choix > 1){
				System.out.println("Voulez-vous creer un nouveau compte [0] ou vous identifier [1]?");
				choix = scan.nextInt();
				scan.nextLine();
			}

			success = false;
			// REGISTER
			if (choix == 0){
				System.out.println("Vous avez decide de creer un nouveau compte.");
				while(!success){
					clientName = askString("Veuillez entrer votre nom");
					pwd = askString("Veuillez entrer votre mot de passe");//TODO password en dur...
					register(clientName,pwd);
					if (!success){
						System.out.println("Nom d'utilisateur ou mdp incorrecte.");
					}
				}		

			} // AUTH
			else{
				System.out.println("Vous avez decide de vous identifier aupres du serveur.");
				while(!success){
					clientName = askString("Veuillez entrer votre nom");
					pwd = askString("Veuillez entrer votre mot de passe");
					auth(clientName,pwd);
					if (!success){
						System.out.println("Nom deja utilise ou format incorrecte");
					}
				}
			}

			// utilisateur est maintenant identifie
			// recuperation de la liste des parties
			msgReader.getMessage();
			success = false;

			// JOIN or CREATE
			boolean firstTurn = true;
			while(!success){
				if (!firstTurn){
					refreshGamesList();
				} else {
					firstTurn = false;
				}

				// 2 choix, soit on cree une nouvelle partie, soit on en rejoins une
				choix = -1;
				while(choix < 0 || choix > 1){
					System.out.println("Voulez-vous creer une partie [0] ou en rejoindre une [1] ?");
					choix = scan.nextInt();
				}
				scan.nextLine();

				String gameName;
				success = false;
				// JOIN
				if (choix == 1){
					System.out.println("Vous avez decide de rejoindre une partie.");
					gameName = askString("Veuillez entrer le nom de la partie : ");
					joinAGame(gameName);

					if (success){ // on a reussi a rejoindre la partie
						System.out.println("Veuillez attendre que la partie debute.");
					}
				} // CREATE 
				else{
					System.out.println("Vous avez decide de creer une partie.");
					// demande au serveur de creer une partie
					int difficulty = 1;
					int nbPlayers = 4;
					int nbSquare = 14;
					createAGame(clientName, difficulty, nbPlayers, nbSquare);

					// traitement de la reponse
					if (success){ // partie cree
						ArrayList<String> players = new ArrayList<String>();
						players.add(clientName);

						currentGame = new Game(nbSquare, difficulty, players, clientName, nbPlayers);
						// on debute la partie quand le joueur le decide
						String command;
						while(!start){
							command = askString("Ecrivez 'start' pour debuter la partie : ");
							start = command.equals("start");
						}
						startGame();
					}
				}
			}

			// la partie a (ou va) debuter, le serveur gere l'execution des tours
			// le client ne fait que reagir aux messages recus jusqu'a la fin du jeu
			while(!endGame){
				msgReader.getMessage();
			}

		} // pas connecte !
		else {
			System.out.println("Fin du programme.");
			System.exit(0);
		}
		*/
	}

	private void startGame(){
		// on envoie le message
		Start startMsg = new Start();
		startMsg.accept(msgHandler);
		// on recupere la reponse
		msgReader.getMessage();
	}

	private void createAGame(String gameName, int diff, int nbPlayers, int nbSquare){
		// envoi du message
		Create createMsg = new Create(gameName, nbPlayers, diff, nbSquare);
		createMsg.accept(msgHandler);

		// recuperation de la reponse
		success = false;
		msgReader.getMessage();
	}

	private void joinAGame(String gameName){
		// envoi du message
		Join joinMsg = new Join(gameName);
		joinMsg.accept(msgHandler);
		// recuperation de la reponse
		msgReader.getMessage();
		if (success){
			currentGame = games.get(0); // TODO choper via le nom
			currentGame.addPlayer(clientName); //TODO possibilite de concurrence ici !!!!
		}
	}

	private void refreshGamesList(){
		// envoi du message
		Refresh refreshMsg = new Refresh();
		refreshMsg.accept(msgHandler);
		// recuperation de la reponse
		msgReader.getMessage();
	}

	private void auth(String name, String pwd){
		// envoi du message
		Auth authMsg = new Auth(name, pwd);
		authMsg.accept(msgHandler);
		// recuperation de la reponse
		msgReader.getMessage();
	}

	private void register(String name, String pwd){
		// envoi du message
		Register registerMsg = new Register(name, pwd);
		registerMsg.accept(msgHandler);

		// recupereation de la reponse
		msgReader.getMessage();
	}

	private String askString(String msg){
		String temp;
		System.out.println(msg);
		temp = scan.nextLine();
		return temp;
	}

	public void setEndGame(boolean b){
		endGame = b;
	}

	public void setSuccess(boolean b){
		success = b;
	}

	public void updateGames(List<Game> games){
		this.games = games;
	}

	public void addPlayerToGame(String name) {
		currentGame.addPlayer(name);
	}

	public void removePlayerFromGame(String name) {
		currentGame.removePlayer(name);
	}

	public void roll(String name) {
		System.out.println("C'est a " + name + "de lancer les des !");
		currentGame.setPlayerTurn(name);

		if (name.equals(clientName)) { // roll si c'est le tour du joueur
			Roll roll = new Roll();
			roll.accept(msgHandler);
		}
	}

	public void movePlayer(int value) {
		System.out.println("Le joueur avance de " + value + " cases.");
		currentGame.movePlayer(value);
	}

	public void selectGame(Map<Integer, String> map) {
		// choisi le mini-jeu voulu
		System.out.println("Veuillez selectionner un mini-jeu parmis les jeux suivants :");
		Collection<String> choices = map.values();
		int i = 0;
		for (String name : choices){
			System.out.println("[" + i + "] : " + name);
			i++;
		}
		int selected = -1;
		while(selected < 0 || selected >= choices.size()){
			selected = scan.nextInt();
			scan.nextLine(); // consume the return line
		}
		// on indique notre reponse
		ChooseGame chooseMsg = new ChooseGame(selected);
		chooseMsg.accept(msgHandler);
	}

	public void startGame(int gameId, int seed) {
		System.out.println("Je dois commencer le jeu dont l'id est " + gameId + " avec un seed de " + seed + ".");
		// pas implementer car on n'a pas encore de mini-jeu
		SendResult resultMsg = new SendResult(42);
		resultMsg.accept(msgHandler);
	}
	
	public String getStatus(){
		return status;
	}
}
