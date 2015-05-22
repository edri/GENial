package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.*;

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
	
	public ResultSet auth(String username, String hashPass) {
		try {
			PreparedStatement sql = conn.prepareStatement("SELECT id, username FROM player WHERE username = ? AND password = ?;");
			sql.setString(1, username);
			sql.setString(2, hashPass);
			return sql.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean playerAlreadyExist(String username, String hashPass) {
		try {
			return auth(username, hashPass).first();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean gameAlreadyExist() {
		try {
			PreparedStatement sql = conn.prepareStatement("SELECT * FROM game;");
			if(sql.executeQuery().first()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int createGame(int playerId, int nbPlayer, int difficulty, int nbSquare) {
		try {
			PreparedStatement sql = conn.prepareStatement("INSERT INTO game(creator, difficulty, squares) VALUES(?, ?, ?)");
			sql.setInt(1, playerId);
			sql.setInt(2, difficulty);
			sql.setInt(3, nbSquare);
			return sql.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public List<Game> fetchGamesList() {
		try {
			PreparedStatement sql = conn.prepareStatement("SELECT id, creator, difficulty, squares FROM game;");
			ResultSet rs = sql.executeQuery();
			List<Game> list = new ArrayList<>();
			while(rs.next()) {
				PreparedStatement ssql = conn.prepareStatement("SELECT player FROM player_game WHERE game = ?;");
				ssql.setInt(1, rs.getInt(1));
				ResultSet srs = ssql.executeQuery();
				
				list.add(new Game(rs.getInt(4), rs.getInt(2), players));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
