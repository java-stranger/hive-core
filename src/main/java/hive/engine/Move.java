package hive.engine;

import java.io.Serializable;

import hive.pieces.Piece;

public class Move implements Serializable {
	
	private static final long serialVersionUID = 7560605941860039091L;

	final Piece piece;
	final Coordinate from, to;

	public Move(Piece p, Coordinate from, Coordinate to) {
		piece = p;
		this.from = from;
		this.to = to;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof Move))
			return false;
		Move move = (Move) other;
		return piece.equals(move.piece) && from == move.from && to == move.to;  
	}
	
	public String toString() {
		return piece + ": " + from + " -> " + to;
	}

}
