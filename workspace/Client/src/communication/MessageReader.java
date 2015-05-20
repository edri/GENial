package communication;

import java.io.BufferedReader;
import java.io.IOException;

import application.App;
import messages.*;

public class MessageReader {
	private BufferedReader in;
	private App application;
	
	public MessageReader(App app){
		this.in = Connection.getInstance().getInputStream();
		application = app;
	}
	
	//TODO return void, traitement des messages en direct
	public void getMessage(){
		String msgJson;
		try {
			String ident = in.readLine();
			switch(ident){
				case Protocol.CMD_ACCEPT:
					application.setSuccess(true);
					break;
				case Protocol.CMD_REFUSE :
					application.setSuccess(false);
					break;
				case Protocol.CMD_GAMES_LIST:
					// deserialise le message
					msgJson = in.readLine();
					GamesList games = null;
					
					// on met a jour la liste des jeux
					application.updateGames(games.getGames());
				default:
					break;
			}
		} catch (IOException e) {
			System.out.println("Deconnecte du serveur...");;
		}
	}
	
	public void setStream(BufferedReader in){
		this.in = in;
	}
}
