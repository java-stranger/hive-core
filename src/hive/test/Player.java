package hive.test;

import java.util.HashSet;

import hive.engine.Position;
import hive.engine.Coordinate;
import hive.engine.Move;
import hive.pieces.Piece;

public class Player extends hive.engine.Player {

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
