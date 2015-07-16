package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;
import hive.engine.PositionUtils;

public class Bug extends Piece {

	private static final long serialVersionUID = -5760446860941592937L;

	public Bug(Player.Color color) {
		super(color, PieceType.BUG);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board, Coordinate current) {
		if(board.canMove(current)) {
			return PositionUtils.getBugMoves(board, current);
		}
		else 
			return empty;
	}

	public boolean canSitOnTopOfOthers() {
		return true;
	}
}
