package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Grasshoper extends Piece {

	private static final long serialVersionUID = 2509198885858960198L;

	Grasshoper(IPlayer.Color color) {
		super(color, PieceType.GRASSHOPER);
	}
	
	@Override
	public HashSet<Coordinate> getMoves(IPosition board, Coordinate current) {
		return PositionUtils.getGrasshoperMoves(board, current);
	}

}
