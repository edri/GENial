package application;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import settings.Settings;
import messages.*;
import miniJeux.MiniJeu;
import miniJeux.challenger.Challenger;
import miniJeux.letterHero.LetterHero;
import communication.*;
import gui.*;

public class App {
	private Game currentGame;
	private GameView gameView;
	private Map<String,MiniJeu> listMiniJeux;
	private List<Lobby> games;
	private Scanner scan;
	private boolean start;
	private boolean endGame;
	private boolean success;
	private MessageHandler msgHandler;
	private MessageReader msgReader;
	private String status;
	private Connection connection;

	private AppFrame mainFrame;

	public App(){
		status = "connection";
		start = false;
		endGame = false;
		currentGame = null;
		games = new ArrayList<Lobby>();
		listMiniJeux = new HashMap<String,MiniJeu>();
		msgHandler = new MessageHandler(this);
		msgReader = new MessageReader(this);
		scan = new Scanner(System.in);

		listMiniJeux.put("LetterHero", new LetterHero(this));
		listMiniJeux.put("Challenger", new Challenger(this));

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
		gameView.setSize(1000, 300);

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
		}).start();*/

		//MiniJeuSelectionFrame radioFrame = new MiniJeuSelectionFrame(this, listMiniJeux);
	}

	public void setEndGame(boolean b){
		endGame = b;
	}

	public void setSuccess(boolean b){
		success = b;
	}

	public void removePlayerFromGame(String name) {
		currentGame.removePlayer(name);
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

	/*
	 * Methodes de préparation de Frame
	 */


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

	private void prepareGame(String gameName, boolean isCreator){
		if (status.equals("onGame")){
			Lobby myLobby = null;
			for ( Lobby l : games){
				if (gameName.equals(l.getName())){
					myLobby = l;
					break;
				}
			}
			// verification normalement inutile mais dans le doute...
			if (myLobby != null){ 
				// int nbCases, int difficulty, ArrayList<String> players, String name, int maxPlayers
				currentGame = new Game(myLobby.getNbSquares(),myLobby.getDifficulty(),myLobby.getPlayers(), myLobby.getName(), myLobby.getMaxPlayers(), msgReader);
				if (isCreator){
					currentGame.setCreator(isCreator);
				}

				gameView = new GameView(currentGame, this);
				Thread gameThread = new Thread(currentGame);
				gameThread.start();
				System.out.println("Vous avez rejoins une partie !! :D");
			}
		}
	}

	/*
	 * Methodes communications serveur
	 */

	public void auth(String name, String pwd){
		if (status.equals("auth")){
			// envoi du message
			success = false;
			Auth authMsg = new Auth(name, pwd);
			authMsg.accept(msgHandler);
			// recuperation de la reponse
			msgReader.getMessage();
			if(success){
				// recuperation de la liste de salon
				msgReader.getMessage();
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
				// recuperation de la liste de salon
				msgReader.getMessage();
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
			System.out.println("test ok join");
			if (success){
				status = "onGame";
				System.out.println("le game list nous a retourne une liste de " + games.size() + " parties");
				prepareGame(gameName, false);
				gameView.display("Veuillez attendre que le createur lance la partie");
			}
		}
	}

	public void createAGame(String gameName, int nbPlayers, int diff, int nbSquare){
		if (status.equals("connected")){
			// envoi du message
			Create createMsg = new Create(gameName, nbPlayers, diff, nbSquare);
			createMsg.accept(msgHandler);

			// le serveur envoie la liste des parties puis le message d'acceptation
			success = false;
			msgReader.getMessage();
			msgReader.getMessage();
			System.out.println("test create ok");
			// traitement en cas de response favorable
			if(success){
				status = "onGame";
				System.out.println("le game list nous a retourne une liste de " + games.size() + " parties");
				prepareGame(gameName, true);
				gameView.display("Demarrer la partie quand vous le souhaitez.");
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

	public void startGame(){
		if (status.equals("onGame")){
			// on envoie le message
			Start startMsg = new Start();
			startMsg.accept(msgHandler);
			// on recupere la reponse
			// message recup dans le run() de game msgReader.getMessage();
		}
	}

	public void begin(){
		if (status.equals("onGame")){
			status = "started";
		}
	}

	public void roll(String name) {
		if (status.equals("started")){
			System.out.println("C'est a " + name + " de lancer les des !");
			gameView.display("Au tour de " + name + " lancer les des.");
			currentGame.setPlayerTurn(name);
			if (name.equals(Settings.userName)) { // roll si c'est le tour du joueur
				Roll roll = new Roll();
				roll.accept(msgHandler);
			}
		}
	}

	public void selectGame(Map<Integer, String> map) {
		if (status.equals("started")){
			// choisi le mini-jeu voulu
			System.out.println("Il y a " + listMiniJeux.size() + " jeux dispo.");
			MiniJeuSelectionFrame selectionFrame = new MiniJeuSelectionFrame(this, listMiniJeux);
		}
	}

	public void chooseGame(String selectedGame){
		// on recherche l'index du jeu
		int index = 0;
		for (String s : listMiniJeux.keySet()){
			if (s.equals(selectedGame)){
				break;
			}
			index++;
		}
		// on indique notre reponse
		ChooseGame chooseMsg = new ChooseGame(index);
		System.out.println("GameId du choose: " + chooseMsg.getGameId());
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

	/*
	 * Autres methodes
	 */

	public void movePlayer(int value) {
		System.out.println("Le joueur avance de " + value + " cases.");
		currentGame.movePlayer(value);
		gameView.display(currentGame.getPlayerTurn() + " avance de " + value + " cases");
	}

	public void updateLobbies(List<Lobby> games){
		this.games = games;
		if (status.equals("connected")){
			mainFrame.updateList();
		}
	}

	public void addPlayerToGame(String name) {
		currentGame.addPlayer(name);
	}

	public List<Lobby> getGameList(){
		return games;
	}

	public String getStatus(){
		return status;
	}
}
