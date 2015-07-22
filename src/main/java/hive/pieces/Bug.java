package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Bug extends Piece {

	private static final long serialVersionUID = -5760446860941592937L;

	public Bug(IPlayer.Color color) {
		super(color, PieceType.BUG);
	}
	
	@Override
	public HashSet<Coordinate> getMoves(IPosition board, Coordinate current) {
		return PositionUtils.getBugMoves(board, current);
	}

	public boolean canSitOnTopOfOthers() {
		return true;
	}
}
