package click;

import java.io.IOException;
import java.util.Observable;
import java.util.Random;

import javax.swing.ImageIcon;

class Slurpeur extends Observable implements Runnable {
	
	static boolean move;
	
	private static int difficulty;

	// Directions possibles
	enum Directions {NORD, SUD, EST, OUEST};
	Directions direction;		// Direction actuelle
	
	// 
	ImageIcon imgN = new ImageIcon("slurpeurN.png");
	ImageIcon imgS = new ImageIcon("slurpeurS.png");
	ImageIcon imgE = new ImageIcon("slurpeurE.png");
	ImageIcon imgO = new ImageIcon("slurpeurO.png");
	
	private int posX;		// Position x
	private int posY;		// Position y
	
	public static Slurpeur instance;
	
	private Slurpeur(int difficutly) throws IOException {
		this.posX = 100;
		this.posY = 100;
		this.difficulty = difficulty;
		move = false;
	}
	
	public static Slurpeur getInstance(int difficutly) {
		if (instance == null) {
			try {
				instance = new Slurpeur(difficulty);
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
			if(posY <= -50) {
				posY = 700;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case SUD:
			this.posY += 1;
			if (posY > 700) {
				posY = -50;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case EST:
			this.posX += 1;
			if (posX > 700) {
				posX = -50;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		case OUEST:
			this.posX -= 1;
			if (posX < -50) {
				posX = 700;
			}
			this.setChanged();
			this.notifyObservers();
			break;
		default:
			break;
		}
	}

	
	public void jump() throws InterruptedException {
			Random direction = new Random();		
			Random pas = new Random();
			
			Directions ancienneDirection = this.direction;
			int nouvelleDirection = direction.nextInt(4);
			
			int nbDePas = pas.nextInt(150) + 100;
			
			Thread.sleep(30);
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
	
	public void moveSlurpeur() {
		move = true;
	}
	
	public void stopSlurpeur() {
		move = false;
	}
	

	public void run() {	
		while (true) { 
			while(true) {
				 // Choisir une direction
		   	 	Random direction = new Random();
				 Directions nouvelleDirection;

				 switch(direction.nextInt(4)) {
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
						Thread.sleep(7 - ( difficulty));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					move(nouvelleDirection);
				}
			}
		}
	}
	
}