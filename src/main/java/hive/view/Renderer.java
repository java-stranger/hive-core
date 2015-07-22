package hive.view;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public interface Renderer {
	
	public void newPiece(Piece p, Coordinate c);
	public void movePiece(Piece p, Coordinate from, Coordinate to);
	public Object removePiece(Piece p, Coordinate c);
	
	public enum HighlightType {
		MY_MOVES,
		ADVERSARY_MOVES,
		BORDER
	}

	void highlight(HashSet<Coordinate> coords, HighlightType type);
	void select(Coordinate coord);
	
	void reset();
}
