package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe repr�sentant la base de donn�es. Elle propose diverses
 * m�thodes permettant au serveur d'enregistrer ou de consulter
 * des donn�es persistantes. Elle est impl�ment�e comme un Singleton.
 *
 */
public class Database {

	// Informations de connexion � la base de donn�es
	public static final String DB_HOST = "jdbc:mysql://localhost:3306/heig_party";
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "";
	
	// Instance unique de la classe (Singleton)
	private static Database instance;

	// Objet repr�sentant la connexion � la base de donn�es
	private Connection conn;
	
	// Restreindre l'acc�s au constructeur pour �viter les instances multiples de
	// cette classe (Singleton)
	private Database() throws SQLException {
		// Initialisation de la connexion � la base de donn�es
		conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
	}
	
	// M�thode permettant de r�cup�rer l'instance unique de cette classe
	public static Database getInstance() throws SQLException {
		// Construire l'instance si n�cessaire
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public int createPlayer(String username, String hash) {
		try {
			PreparedStatement sql = conn.prepareStatement("INSERT INTO player (username, password) VALUES (?, ?)");
			sql.setString(1, username);
			sql.setString(2, hash);
			sql.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e);
		}
		return 1;
	}
}
