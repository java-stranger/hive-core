package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;

public class Bug extends Piece {

	private static final long serialVersionUID = -5760446860941592937L;

	public Bug(Player.Color color) {
		super(color, PieceType.BUG);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this)) {
			return board.getAccesibleForBugNeighboringCells(pos);
		}
		else 
			return empty;
	}

}
