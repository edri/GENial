package miniJeux.slurpeur;

import java.io.IOException;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class SlurpeurMod extends Observable {
	
	private static int difficulty;
	private final int seed;
	
	// Pour le temps
    private Timer timer = new Timer();
    private final int threadTime;
    private final Thread activity;
    
    private TimerTask task;
    private int currentLeftSeconds = 30;
	
	private SlurpeurComponent slurpeur;	// La petite bête sur laquelle il faut cliquer
	private int score = 0;		// Score du joueur
	
	
	
	public SlurpeurMod(int difficulty, int seed) throws IOException {
		this.difficulty = difficulty;
		this.seed = seed;
	    threadTime = (4 - difficulty);
		this.slurpeur = SlurpeurComponent.getInstance(seed);
		
		slurpeur.setRunning(true);
		activity = new Thread();
		
		task = new TimerTask() {
	         @Override
	         public void run() {
	            if (--currentLeftSeconds == -1)
	            {
	            	slurpeur.setRunning(false);
	                timer.cancel();
	                timer.purge();
	                
	                // On indique que l'état de l'objet a été modifié.
	                setChanged();
	                // On notifie les observateurs que l'objet a été modifié.
	                notifyObservers();
	            }
	         }
		};
	}
	
	public void decrementerScore() {
		score -= 5;
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
	
	
   public void startThread() throws LineUnavailableException, IOException, UnsupportedAudioFileException { 
	  Clip music = AudioSystem.getClip();
	  AudioInputStream inputStream = AudioSystem.getAudioInputStream(SlurpeurMod.class.getResourceAsStream("../../musics/Slurpeur.wav"));
	  music.open(inputStream);
	  music.start();
	   
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
