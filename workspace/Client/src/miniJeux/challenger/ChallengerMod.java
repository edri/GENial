package miniJeux.challenger;

import java.awt.Color;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import miniJeux.slurpeur.SlurpeurMod;

/**
 *
 * @author Miguel
 */
public class ChallengerMod extends Observable implements Runnable {

    private final int threadTime;
    private final Thread activity;
    private int currentLeftSeconds = 31;
    private Random random;
    private Timer timer = new Timer();
    private TimerTask task;
    private boolean running;
    private Planet planet;
    private int score = 0;

    public ChallengerMod(int difficulty, int seed) {
        threadTime = (4 - difficulty);
        random = new Random(seed);

        running = true;
        activity = new Thread(this);

        task = new TimerTask() {
            @Override
            public void run() {
               if (--currentLeftSeconds == -1)
               {
                  running = false;
                  timer.cancel();
                  timer.purge();
               }
            }
         };
        
        // Initialisation de la planète
        int posX = random.nextInt(600),
            posY = random.nextInt(400);
        planet = new Planet(this, posX, posY, 100);
        
        // Lancer le jeu
        try {
			startThread();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void move() {
        // On indique que l'état de l'objet a été modifié.
        setChanged();
        // On notifie les observateurs que l'objet a été modifié.
        notifyObservers();
    }
    
    public void planetTouched() {
        // Incrémenter le score, bouger la planète
        score += 200;
        movePlanet();
    }
    
    public void moonTouched() {
        // Malus sur le score, bouger la planète
        if (score >= 100) {
            score -= 100;
        }
        
        // Ajouter une lune à la planète
        planet.add(new Moon(this, 100, 100, 15 + random.nextInt(10) - 10, 100 + random.nextInt(10) - 10, 0.07 - 14 * random.nextDouble()/100, 0));
        
        movePlanet();
    }
           
    private void movePlanet() {
        // Déplacer la planète à un endroit aléatoire
        planet.setPosition(random.nextInt(600), random.nextInt(400));
        
        // Ajouter une lune à la planète
        planet.add(new Moon(this, 100, 100, 15 + random.nextInt(10) - 10, 100 + random.nextInt(10) - 10, 0.07 - 14 * random.nextDouble()/100, 0));
        
        // Changer la couleur de la planète pour faire genre
        planet.setColor(new Color(0, random.nextInt(255), random.nextInt(255)));
        setChanged();
        notifyObservers();
    }

    public void startThread() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    	Clip music = AudioSystem.getClip();
	  	AudioInputStream inputStream = AudioSystem.getAudioInputStream(ChallengerMod.class.getResourceAsStream("Challenger.wav"));
	  	music.open(inputStream);
	  	music.start();
    	
        if (!activity.isAlive()) {
            activity.start();
        }

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(threadTime);
            } catch (InterruptedException e) {
            }
            move();
        }
        
        currentLeftSeconds = 0;
        
        // On indique que l'état de l'objet a été modifié.
        setChanged();
        // On notifie les observateurs que l'objet a été modifié.
        notifyObservers();
    }

    int getCurrentLeftSeconds() {
        return currentLeftSeconds;
    }

    int getScore() {
        return score;
    }

    Planet getPlanet() {
        return planet;
    }
    
    public boolean isGameRunning()
    {
       return running;
    }
}
