package hive.positions;

import hive.player.IPlayer;

public class NoopPositionChecker implements IPositionChecker {

	private static final long serialVersionUID = 3940010807869070167L;

	@Override
	public boolean checkMove(IPosition position, Move m, IPlayer.Color next_turn) {
		return true;
	}

	@Override
	public boolean checkPositionInvariants(IPosition position) {
		return true;
	}
	
	@Override
	public boolean checkUndo(IPosition position, Move m, IPlayer.Color next_turn) {
		return true;
	}


}
