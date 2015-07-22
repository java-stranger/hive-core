package hive.actions;

import hive.engine.Coordinate;
import hive.engine.Game;

public class Move extends Action {
	Move(Coordinate start) {
		super(start);
		type = ActionType.INSERT;
	}

	public boolean check(Game game, Coordinate end) { 
		return piece != null && piece.color() == game.nextPlayer().color() 
				&& piece.getPossibleMoves(game.position, start).contains(end);
	}
	
	public hive.positions.Move createMove(Coordinate end) {
		return new hive.positions.Move(piece, start, end);
	}
}
