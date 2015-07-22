package hive.actions;

import hive.engine.Coordinate;
import hive.engine.Move;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class InsertAction extends Action {
	InsertAction(Coordinate start) {
		super(start);
		type = ActionType.INSERT;
	}

	public boolean check(IPosition position, Coordinate end) { 
		return piece != null && piece.color() == position.nextTurn() 
				&& PositionUtils.getPossibleInsertionCells(position, piece.color()).contains(end);
	}
	
	public Move createMove(Coordinate end) {
		return new Move(piece, null, end);
	}

}
