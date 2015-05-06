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
public class Connection implements Runnable{
	private static Connection instance;
	private Socket socket;
	private boolean running = false;
	private String ip = "";
	private MessageExtractor msgExtractor;
	//private Semaphore writing;

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
			// ferme l'ancienne connection s'il y en avait une ouverte
			
			ip = addrIP;
			// ouvre la nouvelle connection
			try {
				socket = new Socket(ip, Settings.serverPort);

				in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Settings.encoding));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Settings.encoding));
				// se met en attente de nouveau message
				running = true;
				new Thread(this).start();

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return !socket.isClosed();
	}

	public void run(){
		String typeMsg;
		String strJson;
		int charRead;
		
		while(running){
			msgExtractor = new MessageExtractor();
			try {
				// on lit le premier string contenant le type de message
				typeMsg = in.readLine();
				
				// on lit le second string contenant le  json
				strJson = in.readLine();
				
				// on extrait les informations du json et on envoie la reponse adequate
				msgExtractor.process(typeMsg, strJson);
				
			} catch (IOException e) {
				disconnect();
			}
			
		}
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
			running = false;
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return socket.isClosed();
	}

}
