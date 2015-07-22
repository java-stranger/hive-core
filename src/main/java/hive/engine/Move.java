package hive.engine;

import java.io.Serializable;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public final class Move implements Serializable {
	
	private static final long serialVersionUID = 7560605941860039091L;

	final public Piece piece;
	final public Coordinate from, to;

	public Move(Piece p, Coordinate from, Coordinate to) {
		piece = p;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Move))
			return false;
		Move move = (Move) other;
		return piece.equals(move.piece) && from == move.from && to == move.to;  
	}
	
	@Override
	public int hashCode() {
		return 17 + 31 * (piece.hashCode() + 31 * (from.hashCode() + 31 * to.hashCode())); 
	}
	
	@Override
	public String toString() {
		return piece + ": " + from + " -> " + to;
	}

}
