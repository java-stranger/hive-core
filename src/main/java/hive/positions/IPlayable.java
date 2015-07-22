package hive.positions;

import hive.engine.Move;

public interface IPlayable extends IPosition {

	public IPlayable accept(Move m) throws IllegalStateException;
	
	public IPlayable undo(Move m) throws IllegalStateException;

}
