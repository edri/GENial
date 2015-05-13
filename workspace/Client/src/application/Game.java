package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Game {
	private int nbCases;
	private String winner;
	private ArrayList<String> players = new ArrayList<>();
	private Map<String, Integer> positions = new HashMap<>();
	
	public Game(int nbCases, ArrayList<String> players) {
		this.nbCases = nbCases;
		this.players = players;
		
		for (String player : players)
		{
			positions.put(player, 0);
		}
	}
	
	public boolean movePlayer(String name, int moveValue)
	{
		int newPosition = positions.get(name) + moveValue;
		
		if (newPosition >= nbCases)
		{
			positions.replace(name, nbCases);
			winner = name;
			return true;
		}
		else
		{
			positions.replace(name, newPosition);
			return false;
		}
	}
	
	public String getWinner()
	{
		return winner;
	}
	
	public void showStatus()
	{
		System.out.println("Statut de la partie : ");
		
		for (String player : players)
		{
			System.out.println("\t" + player + " : " + positions.get(player) + ".");
		}
	}
}
