package hive.actions;

import hive.engine.Coordinate;
import hive.engine.Move;
import hive.positions.IPosition;

public class MoveAction extends Action {
	MoveAction(Coordinate start) {
		super(start);
		type = ActionType.INSERT;
	}

	public boolean check(IPosition position, Coordinate end) { 
		return piece != null && piece.color() == position.nextTurn() 
				&& piece.getPossibleMoves(position, start).contains(end);
	}
	
	public Move createMove(Coordinate end) {
		return new Move(piece, start, end);
	}
}
