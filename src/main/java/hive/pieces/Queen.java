package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Queen extends Piece {

	private static final long serialVersionUID = 1887479588000358845L;

	Queen(IPlayer.Color color) {
		super(color, PieceType.QUEEN);
	}
	
	@Override
	public HashSet<Coordinate> getMoves(IPosition board, Coordinate current) {
		return PositionUtils.getQueenMoves(board, current);
	}

}
