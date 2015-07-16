package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;
import hive.engine.PositionUtils;

public class Ant extends Piece {

	private static final long serialVersionUID = -1420556873412200025L;

	public Ant(Player.Color color) {
		super(color, PieceType.ANT);
	}

	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board, Coordinate current) {
		if(board.canMove(current)) {
			return PositionUtils.getAntMoves(board, current);
		}
		else 
			return empty;
	}

}
