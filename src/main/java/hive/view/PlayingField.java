package main.java.hive.view;

import java.util.HashMap;
import java.util.HashSet;

import main.java.hive.engine.Coordinate;
import main.java.hive.engine.Player;
import main.java.hive.pieces.Piece;
import main.java.hive.view.Renderer.HighlightType;

public class PlayingField {
	
	public HashMap<Piece, Tile> tiles = new HashMap<>();
	
	public final HashMap<Player.Color, Hand> hands;
	
	private Renderer renderer;
	
	int width;
	int height;
	
	public PlayingField(int w, int h, HashMap<Player.Color, Hand> hands) {
		width = w;
		height = h;
		this.hands = hands;
	}
	
	public void reset() {
		if(renderer != null)
			tiles.values().forEach((Tile t) -> renderer.forget(t));
		tiles.clear();
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
		if(renderer != null) {
			tiles.values().forEach((Tile t) -> renderer.render(t));
		}
	}
	
	public void movePiece(Piece p, Coordinate from, Coordinate to) {
		Tile t = tiles.computeIfAbsent(p, (Piece) -> new Tile(p)).setPosition(to);;
		
		if(renderer != null) renderer.render(t);
	}

	public void playPiece(Piece p, Coordinate at) {
		// insert means playing piece form player's hand
		assert tiles.containsKey(p);
		Coordinate current = tiles.get(p).position();
		hands.get(p.color()).putBack(current);
		movePiece(p, current, at);
	}

	public void insertPiece(Piece p, Coordinate at) {
		// insert is done only during the initial setup
		assert !tiles.containsKey(p);
		movePiece(p, null, at);
	}

	public void removePiece(Piece p) {
		// remove means put back to player's hand
		assert tiles.containsKey(p);
		movePiece(p, tiles.get(p).position(), hands.get(p.color()).getNextPos());
	}

	public int width() { return width; }
	public int height() { return height; }
	
	public void showPossibleMoves(HashSet<Coordinate> moves) {
		if(renderer != null) {
			renderer.highlight(moves, HighlightType.MY_MOVES);
		}
	}

	public void showAdversaryMoves(HashSet<Coordinate> moves) {
		if(renderer != null) {
			renderer.highlight(moves, HighlightType.ADVERSARY_MOVES);
		}
	}
}
