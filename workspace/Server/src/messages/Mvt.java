package messages;




/**
 * Indique le nombre de cases que le joueur doit parcourir
 */
public class Mvt {
	private int squareToMove;
	
	public Mvt(){
		squareToMove = 0;
	}
	
	public Mvt(int nbSquare){
		squareToMove = nbSquare;
	}

	public int getSquareToMove() {
		return squareToMove;
	}

	public void setSquareToMove(int squareToMove) {
		this.squareToMove = squareToMove;
	}
}