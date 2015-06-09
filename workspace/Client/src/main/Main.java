package main;

import application.App;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import application.Game;
import communication.Connection;

public class Main {

	public static void main(String[] args) {
		Thread mainThread = new Thread(new App());
		mainThread.start();
		
		
		/*truc de Miguel
		boolean gameFinished = false;
		Random rand = new Random();
		ArrayList<String> players = new ArrayList<String>();
		players.add("David");
		players.add("James");
		players.add("Jérôme");
		players.add("Mélanie");
		players.add("Miguel");
		
		Game game = new Game(20, players);
		game.showStatus();
		
		while(!gameFinished)
		{
			for (String player : players)
			{			
				gameFinished = game.movePlayer(player, rand.nextInt(6) + 1);
				
				game.showStatus();
				
				if (gameFinished)
					break;
			}
		}
		
		System.out.println();
		System.out.println(game.getWinner() + " a gagné la partie !");*/
		
	}
}
