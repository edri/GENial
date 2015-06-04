package messages;



/**
 * Permet de demander la creation d'une partie ayant les parametres demande
 */
public class Create {
	private String name;
	private int nbPlayers;
	private int difficulty;
	private int nbCases;
	
	public Create(){
		name = "";
		nbPlayers = 0;
		difficulty = 0;
		nbCases = 0;
	}
	
	public Create(String name, int players, int difficulty, int cases){
		this.name = name;
		nbPlayers = players;
		this.difficulty = difficulty;
		nbCases = cases;
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNbPlayers() {
		return nbPlayers;
	}
	
	public void setNbPlayers(int nbPlayers) {
		this.nbPlayers = nbPlayers;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getNbCases() {
		return nbCases;
	}
	
	public void setNbCases(int nbCases) {
		this.nbCases = nbCases;
	}
	
}