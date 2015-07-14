package hive.view;

import java.util.HashSet;

import hive.engine.Coordinate;

public interface Renderer {
	public void render(Tile t);
	public void forget(Tile t);
	
	public enum HighlightType {
		MY_MOVES,
		ADVERSARY_MOVES
	}

	void highlight(HashSet<Coordinate> coords, HighlightType type);
}
