package hive.actions;

import hive.engine.Coordinate;
import hive.engine.Game;
import hive.positions.PositionUtils;

public class Insert extends Action {
	Insert(Coordinate start) {
		super(start);
		type = ActionType.INSERT;
	}

	public boolean check(Game game, Coordinate end) { 
		return piece != null && piece.color() == game.nextPlayer().color() 
				&& PositionUtils.getPossibleInsertionCells(game.position, piece.color()).contains(end);
	}
	
	public hive.positions.Move createMove(Coordinate end) {
		return new hive.positions.Move(piece, null, end);
	}

}
