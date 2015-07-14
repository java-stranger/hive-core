package main.java.hive.pieces;

import java.util.HashSet;

import main.java.hive.engine.Coordinate;
import main.java.hive.engine.Player;
import main.java.hive.engine.Position;

public class Spider  extends Piece {
	Spider(Player.Color color) {
		super(color, PieceType.SPIDER);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this))
			return board.getAccesibleCells(pos, 3);
		else 
			return empty;
	}

}
