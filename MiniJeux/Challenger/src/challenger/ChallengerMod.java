/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package challenger;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Miguel
 */
public class ChallengerMod extends Observable implements Runnable {

    private final int MIN_NEW_Y = -100;
    private final int MAX_NEW_Y = -500;
    private final int threadTime;
    private final Thread activity;
    private int currentLeftSeconds = 31;
    private int[] yPositions_ = {MIN_NEW_Y, MAX_NEW_Y, (MIN_NEW_Y + MAX_NEW_Y) / 2};
    private int[] xPositions = {20, 175, 325};
    private char[] chars = new char[3];
    private Random random;
    private Timer timer = new Timer();
    private TimerTask task;

    private Planet planet;

    private int score = 0;

    public ChallengerMod(int difficulty, int seed) {
        threadTime = (4 - difficulty);
        random = new Random(seed);

        activity = new Thread(this);

        task = new TimerTask() {
            @Override
            public void run() {
                if (--currentLeftSeconds == -1) {
                    System.exit(0);
                }
            }
        };
        
        // Initialisation de la planète
        int posX = random.nextInt(600),
            posY = random.nextInt(400);
        planet = new Planet(this, posX, posY, 100);
        
        // Lancer le jeu
        startThread();
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

    public void startThread() {
        if (!activity.isAlive()) {
            activity.start();
        }

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(threadTime);
            } catch (InterruptedException e) {
            }
            move();
        }
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
}
