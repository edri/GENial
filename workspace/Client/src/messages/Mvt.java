package messages;

import communication.MessageHandler;


/**
 * Indique le nombre de cases que le joueur doit parcourir
 */
public class Mvt extends Message {
	private int squareToMove;
	
	public Mvt(int nbSquare){
		squareToMove = nbSquare;
	}

	public int getSquareToMove() {
		return squareToMove;
	}

	public void setSquareToMove(int squareToMove) {
		this.squareToMove = squareToMove;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}
