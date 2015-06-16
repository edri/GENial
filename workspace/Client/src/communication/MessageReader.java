package communication;

import java.io.BufferedReader;
import java.io.IOException;

import application.*;
import messages.*;

public class MessageReader {
	private BufferedReader in;
	private App application;
	
	public MessageReader(App app){
		application = app;
	}
	
	//TODO return void, traitement des messages en direct
	public void getMessage(){
		String msgJson;
		try {
			String ident = in.readLine();
			System.out.println("Le client a reçu la commande : " + ident);
			switch(ident){
				case Protocol.CMD_ACCEPT:
					application.setSuccess(true);
					break;
				case Protocol.CMD_REFUSE :
					// deserialise le message
					msgJson = in.readLine();
					Refuse refuse = JsonObjectMapper.parseJson(msgJson, Refuse.class);
					// traite le message
					//TODO normalement plus utilisé application.setSuccess(false);
					application.refused(refuse.getReason());
					System.out.println("Echec : " + refuse.getReason());
					break;
				case Protocol.CMD_GAMES_LIST:
					// deserialise le message
					msgJson = in.readLine();
					System.out.println(msgJson);
					GamesList games = JsonObjectMapper.parseJson(msgJson, GamesList.class);
					// traite le message
					application.updateLobbies(games.getGames());
					break;
				case Protocol.CMD_BEGIN:
					in.readLine();
					application.begin();
					break;
				case Protocol.CMD_NEW_PLAYER:
					// deserialisation du message
					msgJson = in.readLine();
					NewPlayer newPlayer = JsonObjectMapper.parseJson(msgJson, NewPlayer.class);
					// traitement du message
					System.out.println(newPlayer.getNewPlayerName() + " a rejoins la partie !");
					application.addPlayerToGame(newPlayer.getNewPlayerName());
					break;
				case Protocol.CMD_DISCONNECT:
					// deserialisation du message
					msgJson = in.readLine();
					Disconnect disc = JsonObjectMapper.parseJson(msgJson, Disconnect.class);
					// traitement du message
					System.out.println(disc.getLeaverName() + " a quitte la partie !");
					application.removePlayerFromGame(disc.getLeaverName());
					break;
				case Protocol.CMD_WINNER:
					// deserialisation du message
					msgJson = in.readLine();
					Winner winner = JsonObjectMapper.parseJson(msgJson, Winner.class);
					// traitement du message
					System.out.println(winner.getWinnerName() + " est le grand gagnant du jeu !!! ");
					application.setEndGame(true);
					break;
				case Protocol.CMD_WINNER_GAME:
					// deserialisation du message
					msgJson = in.readLine();
					WinnerGame winnerGame = JsonObjectMapper.parseJson(msgJson, WinnerGame.class);
					// traitement du message
					System.out.println(winnerGame.getPlayerName() + " a gagne le mini-jeu avec un score de " + winnerGame.getScore());
					break;
				case Protocol.CMD_DICE:
					// deserialisation du message
					msgJson = in.readLine();
					System.out.println(msgJson);
					Dice dice = JsonObjectMapper.parseJson(msgJson, Dice.class);
					// traitement du message 
					application.roll(dice.getPlayerName());
					break;
				case Protocol.CMD_MVT:
					// deserialisation du message
					msgJson = in.readLine();
					Mvt mvt = JsonObjectMapper.parseJson(msgJson, Mvt.class);
					// traitement du message
					application.movePlayer(mvt.getSquareToMove());
					break;
				case Protocol.CMD_SELECT_GAME:
					// deserialisation du message
					msgJson = in.readLine();
					System.out.println(msgJson);
					SelectGame selectGame = JsonObjectMapper.parseJson(msgJson, SelectGame.class);
					// traitement du message
					application.selectGame(selectGame.getGamesMap());
					break;
				case Protocol.CMD_START_GAME:
					// deserialisation du message
					msgJson = in.readLine();
					StartGame startGame = JsonObjectMapper.parseJson(msgJson, StartGame.class);
					// traitement du message
					application.startGame(startGame.getGameId(), startGame.getSeed());
					break;
				default:
					System.out.println("Commande non reconnue...");
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Deconnecte du serveur...");;
		}
	}
	
	public void setStream(BufferedReader in){
		this.in = in;
	}
}
