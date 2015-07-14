package main.java.hive.engine;

import main.java.hive.pieces.Piece;

public class Move {
	
	final Piece piece;
	final Coordinate from, to;

	public Move(Piece p, Coordinate from, Coordinate to) {
		piece = p;
		this.from = from;
		this.to = to;
	}

}
