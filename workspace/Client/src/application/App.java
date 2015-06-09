package application;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import settings.Settings;
import messages.*;
import miniJeux.MiniJeu;
import miniJeux.challenger.Challenger;
import miniJeux.letterHero.LetterHero;
import communication.*;
import gui.*;

public class App implements Runnable{
	private Game currentGame;
	private ArrayList<MiniJeu> listMiniJeux;
	private List<Lobby> games;
	private Scanner scan;
	private boolean start;
	private boolean endGame;
	private boolean success;
	private MessageHandler msgHandler;
	private MessageReader msgReader;
	private String clientName;
	private String status;
	private Connection connection;

	private AppFrame mainFrame;

	public App(){
		status = "connection"; // TODO utile ? A voir plus tard
		start = false;
		endGame = false;
		currentGame = null;
		clientName = "";
		games = new ArrayList<Lobby>();
		listMiniJeux = new ArrayList<>();
		msgHandler = new MessageHandler(this);
		msgReader = new MessageReader(this);
		scan = new Scanner(System.in);

		listMiniJeux.add(new LetterHero(this));
		listMiniJeux.add(new Challenger(this));
		
		ConnectionFrame temp = new ConnectionFrame(this, "GENial, connection au serveur", "Adresse IP : ", "Port : ", false);
		temp.display("Veuillez entrer l'adresse IP du serveur ainsi que le port sur lequel vous voulez vous connecter.", Color.BLACK);
		mainFrame = temp;
		mainFrame.setVisible(true);

		/*
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
		 */
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
		try {
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
		} catch (IOException e) {
			System.out.println("Impossible de se connecter au serveur, bye !");
			System.exit(0);
		}*/
	}

	private void startGame(){
		// on envoie le message
		Start startMsg = new Start();
		startMsg.accept(msgHandler);
		// on recupere la reponse
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
		
		try {
			listMiniJeux.get(gameId).start(2, seed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendScore(int score) {
		System.out.println("J'ai reçu le score de l'utilisateur : " + score + ".");
		
		SendResult resultMsg = new SendResult(score);
		resultMsg.accept(msgHandler);
	}
	/*
	 * -----------------------------------------------------
	 * METHODE POST GUI ------------------------------------
	 * -----------------------------------------------------
	 */

	public String getStatus(){
		return status;
	}

	public void connectToServer(String addrIP, int port) throws UnknownHostException, IOException{
		connection = Connection.getInstance();
		if (connection.connect(addrIP, port)){ // connection réussie
			msgReader.setStream(connection.getInputStream());
			prepareChoice();
		} else { // connection echouée
			mainFrame.display("Echec de la connexion au serveur.", Color.RED);
		}
	}

	public void prepareChoice(){
		status = "authOrRegister";
		mainFrame.setVisible(false);
		mainFrame = new ConnectionFrame(this, "GENial, s'identifier ou s'enregistrer");
		mainFrame.display("Avez-vous deja un compte ou voulez-vous en creer un nouveau ?", Color.BLACK);
		mainFrame.setVisible(true);
	}

	public void prepareAuth(){
		status = "auth";
		mainFrame.setVisible(false);
		mainFrame = new ConnectionFrame(this, "GENial, s'identifier", "Nom d'utilisateur : ", "Mot de passe : ", true);
		mainFrame.display("Veuillez entrer votre identifiant ainsi que votre mot de passe.", Color.BLACK);
		mainFrame.setVisible(true);
	}

	public void prepareRegistration(){
		status = "register";
		mainFrame.setVisible(false);
		mainFrame = new ConnectionFrame(this, "GENial, s'enregistrer", "Nom d'utilisateur : ", "Mot de passe : ", "Confirmer le mot de passe : ");
		mainFrame.display("Veuillez entrer l'identifiant ainsi que le mot de passe que vous voulez utiliser.", Color.BLACK);
		mainFrame.setVisible(true);
	}

	private void prepareLobbies(){
		status = "connected";
		mainFrame.setVisible(false);
		mainFrame = new LobbiesFrame(this);
		mainFrame.setVisible(true);
	}

	public void auth(String name, String pwd){
		if (status.equals("auth")){
			// envoi du message
			success = false;
			Auth authMsg = new Auth(name, pwd);
			authMsg.accept(msgHandler);
			// recuperation de la reponse
			msgReader.getMessage();
			if(success){
				Settings.userName = name;
				prepareLobbies();
			}
		}
	}

	public void register(String name, String pwd){
		if (status.equals("register")){
			// envoi du message
			success = false;
			Register registerMsg = new Register(name, pwd);
			registerMsg.accept(msgHandler);

			// recupereation de la reponse
			msgReader.getMessage();
			if (success){
				Settings.userName = name;
				prepareLobbies();
			}
		}
	}

	public void refused(String msg){
		if (status.equals("auth") || status.equals("register")){
			mainFrame.display(msg, Color.RED);
		} if (status.equals("connected")){
			//TODO gestion message erreur du serveur si on rejoint/cree une partie
		}
	}

	public void joinAGame(String gameName){
		if (status.equals("connected")){
			// envoi du message
			Join joinMsg = new Join(gameName);
			joinMsg.accept(msgHandler);
			// recuperation de la reponse
			success = false;
			msgReader.getMessage();
			msgReader.getMessage();
			if (success){
				/*
				Lobby temp = games.get(0); // TODO choper via le nom
				currentGame = new Game(temp.getNbSquares(), temp.getDifficulty(), temp.getPlayers(), temp.getName(), temp.getMaxPlayers()); 
				currentGame.addPlayer(clientName); //TODO possibilite de concurrence ici !!!!
				 */
				//TODO blublublu
				status = "onGame";
				System.out.println("Vous avez rejoins une partie !! :D");
			}
		}
	}

	public void createAGame(String gameName, int nbPlayers, int diff, int nbSquare){
		if (status.equals("connected")){
			// envoi du message
			Create createMsg = new Create(gameName, nbPlayers, diff, nbSquare);
			createMsg.accept(msgHandler);

			// recuperation de la reponse
			success = false;
			// le serveur envoie la liste des parties puis le message d'acceptation
			// TODO EST-CE AUSSI LE CAS SI REFUS ?
			msgReader.getMessage();
			msgReader.getMessage();
			// traitement en cas de response favorable
			if(success){
				status = "onGame";
				//TODO gestion en cas d'acceptation -> ouverture de la vue plateau
				System.out.println("La partie a ete cree !!! :D :D ");
			}
		}
	}

	public void refreshGamesList(){
		if (status.equals("connected")){
			// envoi du message
			Refresh refreshMsg = new Refresh();
			refreshMsg.accept(msgHandler);
			// recuperation de la reponse
			msgReader.getMessage();
		}
	}

	public void updateLobbies(List<Lobby> games){
		this.games = games;
		if (status.equals("connected")){
			mainFrame.updateList();
		}
	}

	public List<Lobby> getGameList(){
		return games;
	}
}
