package click;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


class Slurpeur extends Observable implements Runnable {

	enum Directions {NORD, SUD, EST, OUEST};
	Directions direction;
	
	ImageIcon imgN = new ImageIcon("slurpeurN.png");
	ImageIcon imgS = new ImageIcon("slurpeurS.png");
	ImageIcon imgE = new ImageIcon("slurpeurE.png");
	ImageIcon imgO = new ImageIcon("slurpeurO.png");
	
	private int posX;		// Position x
	private int posY;		// Position y
	
	public static Slurpeur instance;
	
	private Slurpeur () throws IOException {
		this.posX = 100;
		this.posY = 100;
		System.out.println("Coucou, je suis un nouveau slurpeur, attrape moi !");
	}
	
	public static Slurpeur getInstance() {
		if (instance == null) {
			try {
				instance = new Slurpeur();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public int getX() {
		return this.posX;
	}
	
	public int getY() {
		return this.posY;
	}
	
	public ImageIcon getImage() {
		switch (this.direction) {
		case NORD:
			return imgN;
		case SUD:
			return imgS;
		case EST:
			return imgE;
		case OUEST:
			return imgO;
		default: 
			return null;
		}
	}
	
	public void move(Directions direction) {
		switch (direction) {
		case NORD:
			this.posY -= 1;
			if(posY <= 0) {
				posY = 700;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case SUD:
			this.posY += 1;
			if (posY > 700) {
				posY = 0;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case EST:
			this.posX += 1;
			if (posX > 700) {
				posX = 0;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case OUEST:
			this.posX -= 1;
			if (posX < 0) {
				posX = 700;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		default:
			break;
		}
	}

	
	public void jum() throws InterruptedException {
			Random direction = new Random();
			int nouvelleDirection = direction.nextInt(3);
			
			Random pas = new Random();
			int nbDePas = pas.nextInt(400) + 100;
			
			Directions ancienneDirection = this.direction;
			
			switch (nouvelleDirection) {
			case (0):			// Nord
				this.direction = Directions.NORD;
				for (int i = 0; i < nbDePas; ++i) {
					this.posY -= 1;
					if(this.posY < 0){
						posY = 700;
					}
					this.setChanged();
					this.notifyObservers();
				}
				break;
			case(1):			// Sud
				this.direction = Directions.SUD;
				for (int i = 0; i < nbDePas; ++i) {
					this.posY += 1;
					if(posY > 700) {
						posY = 0;
					}
					this.setChanged();
					this.notifyObservers();
				}
				break;
			case(2):			// Est
				this.direction = Directions.EST;
				for (int i = 0; i < nbDePas; ++i) {
					this.posX += 1;
					if(posX > 700) {
						posX = 0;
					}
					this.setChanged();
					this.notifyObservers();
				}
				break;
			case(3):				// Ouest
				this.direction = Directions.OUEST;
				for (int i = 0; i < nbDePas; ++i) {
					this.posX -= 1;
					if(posX < 0) {
						posX = 700;
					}
					this.setChanged();
					this.notifyObservers();
				}
				break;
			}	
			this.direction = ancienneDirection;
			this.setChanged();
			this.notifyObservers();
			
	} 
	
	public void run() {
		while (true) {
		   	 // Choisir une direction
	   	 	Random direction = new Random();
			 Directions nouvelleDirection;

			 switch(direction.nextInt(3)) {
			 case 0:
				 nouvelleDirection = Directions.NORD;
				 this.direction = Directions.NORD;
				 break;
			 case 1:
				 nouvelleDirection = Directions.SUD;
				 this.direction = Directions.SUD;
				 break;
			 case 2:
				 nouvelleDirection = Directions.EST;
				 this.direction = Directions.EST;
				 break;
			 case 3:
				 nouvelleDirection = Directions.OUEST;
				 this.direction = Directions.OUEST;
				 break;
			 default:
				 nouvelleDirection = Directions.NORD;
				 this.direction = Directions.NORD;
				 break;
			 }
			 
			for (int i = 0; i < new Random().nextInt(200) + 100; ++i) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				move(nouvelleDirection);
			}
		
		}
			
	}
 	
}



public class ClickMod extends Observable {
	
	// Pour le temps
    private Timer timer = new Timer();
    private final int threadTime;
    private final Thread activity;
    
    private TimerTask task;
    private int currentLeftSeconds = 5;
	
	private Slurpeur slurpeur;	// La petite bête sur laquelle il faut cliquer
	private int score = 0;		// Score du joueur
	
	public ClickMod(int difficulty, int seed) throws IOException {
		this.slurpeur = Slurpeur.getInstance();
		

	    threadTime = (4 - difficulty);
		activity = new Thread();
		
		task = new TimerTask() {
	         @Override
	         public void run() {
	            if (--currentLeftSeconds == -1)
	            {
	               System.exit(0);
	            }
	         }
		};
	}
	
	public void start(int difficulty, int seed) throws InterruptedException {
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
	
	 public void startThread()
	   {      
	      if (!activity.isAlive())
	         activity.start();
	      
	      timer.scheduleAtFixedRate(task, 0, 1000);
	   }
}
