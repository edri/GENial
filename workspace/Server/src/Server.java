/*
 * Moret J√©r√¥me 
 * 29 avr. 2015
 * 
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J√©r√¥me
 */
public class Server {

    private int port;
    private boolean shouldRun;
    private ServerSocket serverSocket;
    private Thread serverThread;
    private LinkedList<Game> games = new LinkedList<>();
    

    public Server(int port) {
        this.port = port;
        
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                shouldRun = true;
                while (shouldRun) {
                    try {
                        System.out.println("Attente de client sur " + serverSocket.getLocalSocketAddress());
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Un nouveau client est arrivÈ ...");
                        ClientWorker worker = new ClientWorker(clientSocket);
                        System.out.println("DÈlegation du travail ‡ un client worker...");
                        Thread clientThread = new Thread(worker);
                        clientThread.start();
                    } catch (IOException ex) {
                        System.out.println("IOException in main server thread, exit: " + ex.getMessage());
                        shouldRun = false;
                    }
                }
            }
        });
        serverThread.start();
    }
    
    public void stopServer() throws IOException {
        shouldRun = false;
        serverSocket.close();
    }
    
    public static void main(String[] args) {
        final int PORT = 7777;
        Server server = new Server(PORT); 
    }

}
