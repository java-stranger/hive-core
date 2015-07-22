package hive.actions;

import hive.engine.Coordinate;
import hive.engine.Move;
import hive.pieces.Piece;
import hive.positions.IPosition;

public abstract class Action {
	protected final Coordinate start;
	
	public static enum ActionType {
		INSERT,
		MOVE
	};
	
	protected ActionType type;
	protected Piece piece = null;
	
	public static Action create(ActionType type, Coordinate start) {
		switch(type) {
		case INSERT:
			return new InsertAction(start);
		case MOVE:
			return new MoveAction(start);
		}
		return null;
	}

	public static Action create(ActionType type, Coordinate start, Piece p) {
		return create(type, start).setPiece(p);
	}

	abstract public boolean check(IPosition position, Coordinate end); 
	
	public Piece getPiece() {
		return piece;
	}

	public Action setPiece(Piece piece) {
		this.piece = piece;
		return this;
	}

	public Action(Coordinate start) {
		this.start = start;
	}
	
	public Action setType(ActionType type) {
		this.type = type;
		return this;
	}

	public Coordinate getStart() {
		return start;
	}

	public ActionType getType() {
		return type;
	}
	
	abstract public Move createMove(Coordinate end);
	
	public String toString() {
		return type + " " + piece + ": started@" + start;
	}
}
