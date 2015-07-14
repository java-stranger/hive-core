package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;

public class Queen extends Piece {
	Queen(Player.Color color) {
		super(color, PieceType.QUEEN);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this))
			return board.getAccesibleNeighboringCells(pos);
		else 
			return empty;
	}

}
