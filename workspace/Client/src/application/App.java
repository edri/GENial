package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import messages.*;
import communication.*;

public class App implements Runnable{
	private Game currentGame;
	private List<Game> games;
	private boolean start;
	private boolean endGame;
	private boolean success;
	private MessageHandler msgHandler;
	private MessageReader msgReader;
	
	public App(){
		start = false;
		endGame = false;
		currentGame = null;
		games = new ArrayList<Game>();
		msgHandler = new MessageHandler(this);
		msgReader = new MessageReader(this);
	}
	
	@Override
	public void run() {
		int choix;
		String name;
		String pwd;
		String addrIP;
		Connection connection;
		
		Scanner scan = new Scanner(System.in);

		System.out.println("Bonjour, bienvenue sur cette super application!");

		// demande de l'adresse IP du serveur et connexion
		System.out.println("Veuillez entrer l'adresse IP du serveur [format : xxx.xxx.xxx.xxx]");
		addrIP = scan.nextLine();
		connection = Connection.getInstance();
		// connection au serveur
		if (connection.connect(addrIP)){
			// utilisateur doit s'identifier ou creer un nouveau compte
			choix = -1;
			while(choix < 0 || choix > 1){
				System.out.println("Voulez-vous creer un nouveau compte [0] ou vous identifier [1]?");
				choix = scan.nextInt();
			}

			success = false;
			// REGISTER
			if (choix == 0){
				System.out.println("Vous avez decide de creer un nouveau compte.");
				while(!success){
					name = askString("Veuillez entrer votre nom", scan);
					pwd = askString("Veuillez entrer votre mot de passe", scan);//TODO password en dur...
					register(name,pwd);
					if (!success){
						System.out.println("Nom d'utilisateur ou mdp incorrecte.");
					}
				}		

			} // AUTH
			else{
				System.out.println("Vous avez decide de vous identifier aupres du serveur.");
				while(!success){
					name = askString("Veuillez entrer votre nom", scan);
					pwd = askString("Veuillez entrer votre mot de passe", scan);
					auth(name,pwd);
					if (!success){
						System.out.println("Nom deja utilise ou format incorrecte");
					}
				}
			}

			// utilisateur identifie, acces au salon

			// le serveur envoi la liste des parties cree (action dans MessageReader)

			// 2 choix, soit on rejoins une nouvelle partie, soit on en rejoins une
			choix = -1;
			while(choix < 0 || choix > 1){
				System.out.println("Voulez-vous creer une partie [0] ou en rejoindre une [1] ?");
				choix = scan.nextInt();
			}

			String gameName;
			success = false;
			// on rejoins une partie
			if (choix == 1){
				System.out.println("Vous avez decide de rejoindre une partie.");
				while(!success){
					gameName = askString("Veuillez entrer le nom de la partie : ", scan);
					success = joinAGame(gameName);
				}
			} // on cree une partie 
			else{
				System.out.println("Vous avez decide de creer une partie.");
				while(!success){
					gameName = askString("Veuillez entrer le nom de la partie : ", scan);
					int difficulty = 1;
					int nbPlayers = 4;
					int nbSquare = 14;
					createAGame(gameName, difficulty, nbPlayers, nbSquare);
				}
				// on debute la partie
				String command;
				while(!start){
					command = askString("Ecrivez 'start' pour debuter la partie : ",scan);
					start = command.equals("start");
				}
			}
			
			// attente active sur le lancement de la partie
			System.out.println("Veuillez attendre que la partie debute.");
			while(!start){ }
			
			while(!endGame){
				msgReader.getMessage();
			}

		} // pas connecte !
		else {
			System.out.println("Fin du programme.");
			System.exit(0);
		}
	}

	private void startGame(){

	}

	private boolean createAGame(String gameName, int diff, int nbPlayers, int nbSquare){
		return false;
	}

	private boolean joinAGame(String gameName){
		return false;
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

	private String askString(String msg, Scanner scan){
		String temp;
		System.out.println(msg);
		temp = scan.nextLine();
		return temp;
	}
	
	public void setSuccess(boolean b){
		success = b;
	}
	
	public void updateGames(List<Game> games){
		this.games = games;
	}
}
