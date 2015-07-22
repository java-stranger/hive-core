package hive.positions;

import java.io.Serializable;

import hive.player.IPlayer;

public interface IPositionChecker extends  Serializable {

	boolean checkMove(IPosition position, Move m, IPlayer.Color next_turn);

	boolean checkUndo(IPosition position, Move m, IPlayer.Color next_turn);

	boolean checkPositionInvariants(IPosition position);

}
