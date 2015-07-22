package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Ant extends Piece {

	private static final long serialVersionUID = -1420556873412200025L;

	public Ant(IPlayer.Color color) {
		super(color, PieceType.ANT);
	}

	@Override
	public HashSet<Coordinate> getMoves(IPosition board, Coordinate current) {
		return PositionUtils.getAntMoves(board, current);
	}

}
