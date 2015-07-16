package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;
import hive.engine.PositionUtils;

public class Queen extends Piece {

	private static final long serialVersionUID = 1887479588000358845L;

	Queen(Player.Color color) {
		super(color, PieceType.QUEEN);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board, Coordinate current) {
		if(board.canMove(current))
			return PositionUtils.getQueenMoves(board, current);
		else 
			return empty;
	}

}
