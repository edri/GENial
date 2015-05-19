package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import settings.*;

// singleton, facilite d'acces et une seule connection par client
public class Connection {
	private static Connection instance;
	private Socket socket;
	private String ip = "";

	// echange de strings -> reader/writer
	private BufferedReader in;
	private PrintWriter out;

	private Connection(){	}

	public static Connection getInstance(){
		if (instance == null){
			instance = new Connection();
		}
		return instance;
	}

	public boolean connect(String addrIP){
		// connection a un nouveau serveur
		if (!ip.equals(addrIP)){
			// ferme l'ancienne connexion si existante
			if (!ip.equals("")){
				disconnect();
			}
			
			// ouvre la nouvelle connection
			ip = addrIP;
			try {
				socket = new Socket(ip, Settings.serverPort);

				in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Settings.encoding));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Settings.encoding));
			} catch (UnknownHostException e) {
				System.out.println("Adresse IP inconnue!");
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Impossible de se connecter au serveur!");
				//e.printStackTrace();
			}
		}
		return socket == null ? false : !socket.isClosed();
	}
	
	public synchronized void sendMsg(String ident, String msgJson){
		out.write(ident);
		out.println();
		out.write(msgJson);
		out.println();
		out.flush();
	}

	public boolean disconnect(){
		// essaie de fermer la connection
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Deconnecte du serveur...");
			//e.printStackTrace();
		}
		return socket == null ? true : !socket.isClosed();
	}
	
	public BufferedReader getInputStream(){
		return in;
	}
	
	public PrintWriter getOutputStream(){
		return out;
	}

}
