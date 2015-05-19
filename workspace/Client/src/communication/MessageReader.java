package communication;

import java.io.BufferedReader;
import java.io.IOException;

import messages.*;

public class MessageReader {
	private BufferedReader in;
	
	public MessageReader(){
		this.in = Connection.getInstance().getInputStream();
	}
	
	public Message getMessage(){
		Message msg = null;
		try {
			String ident = in.readLine();
			switch(ident){
				case Protocol.CMD_ACCEPT:
					break;
				case Protocol.CMD_REFUSE :
					break;
				// etc...
				default:
					break;
			}
		} catch (IOException e) {
			System.out.println("Deconnecte du serveur...");;
		}
		
		return msg;
	}
	
	public void setStream(BufferedReader in){
		this.in = in;
	}
}
