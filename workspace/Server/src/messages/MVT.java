package messages;


/**
 * Indique le nombre de cases que le joueur doit parcourir
 */
public class MVT {
	private int squareToMove;
	
	public MVT(int nbSquare){
		squareToMove = nbSquare;
	}

	public int getSquareToMove() {
		return squareToMove;
	}

	public void setSquareToMove(int squareToMove) {
		this.squareToMove = squareToMove;
	}
}
