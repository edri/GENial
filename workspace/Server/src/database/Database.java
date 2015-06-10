package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.*;

/**
 * Classe représentant la base de données. Elle propose diverses méthodes
 * permettant au serveur d'enregistrer ou de consulter des données persistantes.
 * Elle est implémentée comme un Singleton.
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

	// Restreindre l'accès au constructeur pour éviter les instances multiples
	// de
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
			PreparedStatement sql = conn
					.prepareStatement("INSERT INTO player (username, password) VALUES (?, ?)");
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
			PreparedStatement sql = conn
					.prepareStatement("SELECT id, username FROM player WHERE username = ? AND password = ?;");
			sql.setString(1, username);
			sql.setString(2, hashPass);
			return sql.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean playerAlreadyExist(String username) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT username FROM player;");
			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				if(rs.getString(1).contains(username))
					return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Map<Integer, String> getGamesMap() {
		Map<Integer, String> map = new HashMap<>();
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM mini_game;");
			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt(1), rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public String getPlayerNameFromId(int id) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT username FROM player WHERE id = ?;");
			sql.setInt(1, id);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getPlayerIdFromName(String name) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT id FROM player WHERE username = ?;");
			sql.setString(1, name);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
