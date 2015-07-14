package main.java.hive.test;

import java.util.HashSet;

import main.java.hive.engine.Coordinate;
import main.java.hive.engine.Move;
import main.java.hive.engine.Position;
import main.java.hive.pieces.Piece;

public class Player extends main.java.hive.engine.Player {

	public Player(Color color) {
		super(color);
	}
	
	public Move nextMove(Position board) {
		
		Piece p = getPiece(null); // choose the first one
		
		if(p == null) return null;
		
		HashSet<Coordinate> positions = board.getPossibleInsertionCells(this.color());
		
		return new Move(p, null, positions.iterator().next());
	}

}
