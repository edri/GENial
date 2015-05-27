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
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM game;");
			if (sql.executeQuery().first()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int createGame(int playerId, String name, int nbPlayer, int difficulty,
			int nbSquare) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("INSERT INTO game(creator, difficulty, squares, max_players, name) VALUES(?, ?, ?, ?, ?)");
			sql.setInt(1, playerId);
			sql.setInt(2, difficulty);
			sql.setInt(3, nbSquare);
			sql.setInt(4, nbPlayer);
			sql.setString(5, name);
			return sql.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
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
	
	public int getMyGameDifficulty(int playerId) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT difficulty FROM game WHERE creator = ?;");
			sql.setInt(1, playerId);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public ArrayList<String> playerInGame(int gameId) throws SQLException {
		ArrayList<String> players = new ArrayList<>();
		PreparedStatement sql = conn
				.prepareStatement("SELECT player FROM player_game WHERE game = ?;");
		sql.setInt(1, gameId);
		ResultSet rs = sql.executeQuery();
		while (rs.next()) {
			PreparedStatement ssql = conn
					.prepareStatement("SELECT username FROM player WHERE id = ?;");
			ssql.setInt(1, rs.getInt(1));
			ResultSet srs = ssql.executeQuery();
			srs.next();
			players.add(srs.getString(1));
		}
		return players;
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
	
	public ArrayList<Game> fetchGamesList() {
		ArrayList<Game> list = new ArrayList<>();
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT id, creator, difficulty, squares, max_players, name FROM game;");
			ResultSet rs = sql.executeQuery();
			ArrayList<String> players = new ArrayList<>();
			while (rs.next()) {
				players = playerInGame(rs.getInt(1));
				list.add(new Game(rs.getInt(4), rs.getInt(3), players, rs.getString(6), rs.getInt(5)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
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
	
	public int getMaxPlayersInGame(int gameId) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT max_players FROM game WHERE id = ?;");
			sql.setInt(1, gameId);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getNbPlayersInGame(int gameId) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT count(*) FROM player_game WHERE game = ?;");
			sql.setInt(1, gameId);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	// Ajouter un joueur à une partie
	public boolean addPlayerToGame(int player, String gameName) {
		try {
			int game = getGameIdFromName(gameName);
			if(getNbPlayersInGame(game) >= getMaxPlayersInGame(game))
				return false;
			
			PreparedStatement sql = conn
					.prepareStatement("INSERT INTO player_game (game, player) VALUES (?, ?)");
			sql.setInt(1, game);
			sql.setInt(2, player);
			sql.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public int getGameIdFromName(String gameName) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("SELECT id FROM game WHERE name = ?;");
			sql.setString(1, gameName);
			ResultSet rs = sql.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// Retire un joueur d'une partie
	public void removePlayerFromGame(int player, int game) {
		try {
			PreparedStatement sql = conn
					.prepareStatement("DELETE FROM player_game WHERE player = ? AND game = ?");
			sql.setInt(1, player);
			sql.setInt(2, game);
			sql.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
