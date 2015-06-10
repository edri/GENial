package miniJeux.challenger;

import java.awt.Color;
import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
        
        // Initialisation de la plan�te
        int posX = random.nextInt(600),
            posY = random.nextInt(400);
        planet = new Planet(this, posX, posY, 100);
        
        // Lancer le jeu
        startThread();
    }

    private void move() {
        // On indique que l'�tat de l'objet a �t� modifi�.
        setChanged();
        // On notifie les observateurs que l'objet a �t� modifi�.
        notifyObservers();
    }
    
    public void planetTouched() {
        // Incr�menter le score, bouger la plan�te
        score += 200;
        movePlanet();
    }
    
    public void moonTouched() {
        // Malus sur le score, bouger la plan�te
        if (score >= 100) {
            score -= 100;
        }
        
        // Ajouter une lune � la plan�te
        planet.add(new Moon(this, 100, 100, 15 + random.nextInt(10) - 10, 100 + random.nextInt(10) - 10, 0.07 - 14 * random.nextDouble()/100, 0));
        
        movePlanet();
    }
           
    private void movePlanet() {
        // D�placer la plan�te � un endroit al�atoire
        planet.setPosition(random.nextInt(600), random.nextInt(400));
        
        // Ajouter une lune � la plan�te
        planet.add(new Moon(this, 100, 100, 15 + random.nextInt(10) - 10, 100 + random.nextInt(10) - 10, 0.07 - 14 * random.nextDouble()/100, 0));
        
        // Changer la couleur de la plan�te pour faire genre
        planet.setColor(new Color(0, random.nextInt(255), random.nextInt(255)));
        setChanged();
        notifyObservers();
    }

    public void startThread() {
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
        
        // On indique que l'�tat de l'objet a �t� modifi�.
        setChanged();
        // On notifie les observateurs que l'objet a �t� modifi�.
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
