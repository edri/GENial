package miniJeux.slurpeur;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class SlurpeurMod extends Observable {
	
	private static int difficulty;
	private final int seed;
	
	// Pour le temps
    private Timer timer = new Timer();
    private final int threadTime;
    private final Thread activity;
    
    private TimerTask task;
    private int currentLeftSeconds = 5;
	
	private SlurpeurComponent slurpeur;	// La petite bête sur laquelle il faut cliquer
	private int score = 0;		// Score du joueur
	
	
	
	public SlurpeurMod(int difficulty, int seed) throws IOException {
		this.difficulty = difficulty;
		this.seed = seed;
	    threadTime = (4 - difficulty);
		this.slurpeur = SlurpeurComponent.getInstance();
		
		slurpeur.setRunning(true);
		activity = new Thread();
		
		task = new TimerTask() {
	         @Override
	         public void run() {
	            if (--currentLeftSeconds == -1)
	            {
	            	slurpeur.setRunning(false);
	            	currentLeftSeconds = 0;
	                timer.cancel();
	                timer.purge();
	            }
	         }
		};
	}
	
	public static int getDifficulty() {
		return difficulty;
	}
	
	public void start() throws InterruptedException {
		this.slurpeur.run();
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void incrementerScore() {
		this.score += 10;
		System.out.println("+10");
	}
	
	public int getCurrentLeftSeconds() {
	      return currentLeftSeconds;
	}
	
	
   public void startThread() {      
      if (!activity.isAlive()) {
         activity.start();
      }   
      timer.scheduleAtFixedRate(task, 0, 1000);
   }
   
   public boolean isGameRunning()
   {
      return slurpeur.isSlurpeurRunning();
   }
   
}
