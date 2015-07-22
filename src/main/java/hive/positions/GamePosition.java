package hive.positions;

public interface GamePosition {

	public GamePosition accept(Move m) throws IllegalStateException;
	
	public GamePosition undo(Move m) throws IllegalStateException;

}
