package hive.positions;

import hive.engine.Coordinate;
import hive.player.IPlayer;

public class PositionChecker implements IPositionChecker {

	private static final long serialVersionUID = 5309242371992161079L;

	@Override
	public boolean checkMove(IPosition position, Move move, IPlayer.Color next_turn) {
		if(move.from != null && !move.piece.equals(position.getTopPieceAt(move.from))) {
			throw new IllegalStateException("The top level piece " + position.getTopPieceAt(move.from) 
						+ "@"+ move.from + " is different from expected "+ move.piece + "!");
		}
		if(!position.isFree(move.to) && !move.piece.canSitOnTopOfOthers()) {
			throw new IllegalStateException("Cannot move/insert "+ move.piece + " to "+ move.to 
					+ ": already occupied by " + position.getTopPieceAt(move.to) + "!");
		}
		
		for(Coordinate n : move.to.getNeighbors()) {
			if(!position.isFree(n)) { // check if the destination is "attached" to other pieces
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean checkUndo(IPosition position, Move move, IPlayer.Color next_turn) {
		if(!move.piece.equals(position.getTopPieceAt(move.to))) {
			throw new IllegalStateException("The top level piece " + position.getTopPieceAt(move.to) 
						+ "@"+ move.to + " is different from expected "+ move.piece + "!");
		}
		if(move.from != null && !position.isFree(move.from) && !move.piece.canSitOnTopOfOthers()) {
			throw new IllegalStateException("Cannot undo move "+ move.piece + " to "+ move.from 
					+ ": already occupied by " + position.getTopPieceAt(move.from) + "!");
		}
		
		for(Coordinate n : move.from.getNeighbors()) {
			if(!position.isFree(n)) { // check if the destination is "attached" to other pieces
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean checkPositionInvariants(IPosition position) {
		if(!PositionUtils.connected(position))
			throw new IllegalStateException("Would result in disconnected position!");
		
		return true;
	}

}
