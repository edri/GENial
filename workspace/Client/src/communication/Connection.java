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

	public boolean connect(String addrIP, int port) throws UnknownHostException, IOException {
		// verification si on cherche une connection a un nouveau serveur ou sur un nouveau port
		if (!(ip.equals(addrIP) && port == Settings.serverPort)){
			// ferme l'ancienne connexion si existante
			if (!ip.equals("")){
				disconnect();
			}

			// ouvre la nouvelle connection
			ip = addrIP;
			socket = new Socket(ip, port);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Settings.encoding));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Settings.encoding));
			Settings.serverPort = port;
			Settings.addrIP = addrIP;
		}
		return socket == null ? false : !socket.isClosed();
	}

	public synchronized void sendMsg(String ident, String msgJson){
		out.println(ident);
		out.println(msgJson);
		out.flush();
	}

	public synchronized void sendMsg(String ident){
		out.println(ident);
		out.flush();
	}

	public boolean disconnect() throws IOException{
		// essaie de fermer la connection si elle existe
		if (socket != null){
			socket.close();
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
