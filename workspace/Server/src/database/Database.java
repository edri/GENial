package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe représentant la base de données. Elle propose diverses
 * méthodes permettant au serveur d'enregistrer ou de consulter
 * des données persistantes. Elle est implémentée comme un Singleton.
 *
 */
public class Database {

	// Informations de connexion à la base de données
	public static final String DB_HOST = "jdbc:mysql://localhost:3306/heig_party";
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "";
	
	// Instance unique de la classe (Singleton)
	private static Database instance;

	// Objet représentant la connexion à la base de données
	private Connection conn;
	
	// Restreindre l'accès au constructeur pour éviter les instances multiples de
	// cette classe (Singleton)
	private Database() throws SQLException {
		// Initialisation de la connexion à la base de données
		conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
	}
	
	// Méthode permettant de récupérer l'instance unique de cette classe
	public static Database getInstance() throws SQLException {
		// Construire l'instance si nécessaire
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
