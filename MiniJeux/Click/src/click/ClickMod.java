package click;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class ClickMod extends Observable {
	
	private static int difficulty;
	private final int seed;
	
	// Pour le temps
    private Timer timer = new Timer();
    private final int threadTime;
    private final Thread activity;
    
    private TimerTask task;
    private int currentLeftSeconds = 30;
	
	private Slurpeur slurpeur;	// La petite bête sur laquelle il faut cliquer
	private int score = 0;		// Score du joueur
	
	
	
	public ClickMod(int difficulty, int seed) throws IOException {
		this.difficulty = difficulty;
		this.seed = seed;
	    threadTime = (4 - difficulty);
		this.slurpeur = Slurpeur.getInstance();
		activity = new Thread();
		
		task = new TimerTask() {
	         @Override
	         public void run() {
	            if (--currentLeftSeconds == -1)
	            {          
	               System.exit(0); // A changer pour que l'appli ne se ferme pas sauvagement
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
   
}
