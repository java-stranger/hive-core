package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;
import hive.engine.PositionUtils;

public class Grasshoper extends Piece {

	private static final long serialVersionUID = 2509198885858960198L;

	Grasshoper(Player.Color color) {
		super(color, PieceType.GRASSHOPER);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board, Coordinate current) {
		if(board.canMove(current)) {			
			return PositionUtils.getGrasshoperMoves(board, current);
		}
		else 
			return empty;
	}

}
