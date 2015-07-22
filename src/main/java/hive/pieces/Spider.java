package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Spider  extends Piece {

	private static final long serialVersionUID = 9221240447286666043L;

	Spider(IPlayer.Color color) {
		super(color, PieceType.SPIDER);
	}
	
	@Override
	public HashSet<Coordinate> getMoves(IPosition board, Coordinate current) {
		return PositionUtils.getSpiderMoves(board, current);
	}

}
