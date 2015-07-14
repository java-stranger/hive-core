package hive.view;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public class Tile {
	Coordinate pos;
	
	final Piece piece;

	public Tile(Piece p) {
		this(p, Coordinate.axial(0, 0));
	}
	
	public Tile(Piece p, Coordinate c) {
		this.piece = p;
		this.pos = c;
	}
	
	public Piece getPiece() {
		return piece;
	}	

	public Coordinate position() { return pos; }
	public Tile setPosition(Coordinate newPos) { 
		pos = newPos; 
		return this; 
	}
}
