package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;
import hive.engine.PositionUtils;

public class Spider  extends Piece {

	private static final long serialVersionUID = 9221240447286666043L;

	Spider(Player.Color color) {
		super(color, PieceType.SPIDER);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board, Coordinate current) {
		if(board.canMove(current))
			return PositionUtils.getSpiderMoves(board, current);
		else 
			return empty;
	}

}
