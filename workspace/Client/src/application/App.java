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

import javax.swing.JOptionPane;

import settings.Settings;
import messages.*;
import miniJeux.MiniJeu;
import miniJeux.challenger.Challenger;
import miniJeux.letterHero.LetterHero;
import miniJeux.slurpeur.Slurpeur;
import communication.*;
import gui.*;

public class App {
	private Game currentGame;
	private GameView gameView;
	private Map<String,MiniJeu> listMiniJeux;
	private String[] gameName;
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
	private Thread gameThread;

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

		gameName = new String[3];
		gameName[0] = "LetterHero";
		gameName[1] = "Challenger";
		gameName[2] = "Slurpeur";
		listMiniJeux.put("LetterHero", new LetterHero(this));
		listMiniJeux.put("Challenger", new Challenger(this));
		listMiniJeux.put("Slurpeur", new Slurpeur(this));

		ConnectionFrame temp = new ConnectionFrame(this, "GENial, connection au serveur", "Adresse IP : ", "Port : ", false);
		temp.display("Veuillez entrer l'adresse IP du serveur ainsi que le port sur lequel vous voulez vous connecter.", Color.BLACK);
		mainFrame = temp;
		mainFrame.setVisible(true);
		
		try {
			listMiniJeux.get("Slurpeur").start(2, 28);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				gameThread = new Thread(currentGame);
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
				new RollFrame(this,"C'est a votre tour de lancer les des !");
			}
		}
	}

	public void sendRoll(){
		if (status.equals("started")){
			Roll roll = new Roll();
			roll.accept(msgHandler);
		}
	}

	public void selectGame(Map<Integer, String> map) {
		if (status.equals("started")){
			// choisi le mini-jeu voulu
			System.out.println("Il y a " + listMiniJeux.size() + " jeux dispo.");
			MiniJeuSelectionFrame selectionFrame = new MiniJeuSelectionFrame(this, listMiniJeux, gameName);
		}
	}

	public void chooseGame(String selectedGame){
		if (status.equals("started")){
			// on recherche l'index du jeu
			int index = 0;
			for (; index < gameName.length; index++){
				if (gameName[index].equals(selectedGame)){
					break;
				}
			}
			// on indique notre reponse
			ChooseGame chooseMsg = new ChooseGame(index);
			System.out.println("GameId du choose: " + chooseMsg.getGameId());
			chooseMsg.accept(msgHandler);
		}
	}

	public void startGame(int gameId, int seed) {
		if(status.equals("started")){
			System.out.println("Je dois commencer le jeu dont l'id est " + gameId + " avec un seed de " + seed + ".");

			try {
				listMiniJeux.get(gameName[gameId]).start(currentGame.getDifficulty(), seed);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendScore(int score, MiniJeu miniJeu) {
		if (status.equals("started")){
			System.out.println("J'ai reçu le score de l'utilisateur : " + score + ".");
			miniJeu.finish();
			SendResult resultMsg = new SendResult(score);
			resultMsg.accept(msgHandler);
		}
	}

	public void endGame(){
		status = "connected";
		gameView.dispose();
		currentGame.setFinished(true);
		if (gameThread.isAlive()){
			gameThread.stop();
		}
	}

	/*
	 * Autres methodes
	 */

	public void movePlayer(int value) {
		if (status.equals("started")){
			System.out.println("Le joueur avance de " + value + " cases.");
			currentGame.movePlayer(value);
			gameView.display(currentGame.getPlayerTurn() + " avance de " + value + " cases");
		}
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

	public void removePlayerFromGame(String name) {
		currentGame.removePlayer(name);
	}

	public List<Lobby> getGameList(){
		return games;
	}

	public String getStatus(){
		return status;
	}

	public void setSuccess(boolean b){
		success = b;
	}

	public void setEndGame(boolean b){
		endGame = b;
		if (endGame){
			endGame();
		}
	}
}
