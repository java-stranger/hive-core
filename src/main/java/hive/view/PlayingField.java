package hive.view;

import java.util.HashMap;
import java.util.HashSet;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer;
import hive.view.Renderer.HighlightType;

public class PlayingField {
	
	public final HashMap<IPlayer.Color, Hand> hands;
	
	private Renderer renderer;
	
	int width;
	int height;
	
	public PlayingField(int w, int h, HashMap<IPlayer.Color, Hand> hands) {
		width = w;
		height = h;
		this.hands = hands;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void movePiece(Piece p, Coordinate from, Coordinate to) {
		if(renderer != null) renderer.movePiece(p, from, to);		
		hands.values().forEach((Hand h) -> h.rearrangeToAvoidClash(1, renderer));
	}

	public void playPiece(Piece p, Coordinate at) {
		// insert means playing piece form player's hand
		Coordinate current = hands.get(p.color()).removeFromHandAnyOfType(p);
		movePiece(p, current, at);
	}

	public void insertPiece(Piece p, Coordinate at) {
		// insert is done only during the initial setup
		if(renderer != null) renderer.newPiece(p, at);
	}

	public void removePiece(Piece p, Coordinate at) {
		// remove means put back to player's hand
		movePiece(p, at, hands.get(p.color()).placeInHand(p));
	}

	public int width() { return width; }
	public int height() { return height; }
	
	public void showPossibleMoves(HashSet<Coordinate> moves) {
		if(renderer != null) {
			renderer.highlight(moves, HighlightType.MY_MOVES);
//			renderer.highlight(PositionUtils.path, HighlightType.MY_MOVES);
		}
	}

	public void showAdversaryMoves(HashSet<Coordinate> moves) {
		if(renderer != null) {
			renderer.highlight(moves, HighlightType.ADVERSARY_MOVES);
		}
	}
	
	public void setSelected(Coordinate c) {
		if(renderer != null) {
			renderer.select(c);
		}		
	}
	
	public void displayExternalBorder(HashSet<Coordinate> cells) {
		if(renderer != null) {
			renderer.highlight(cells, HighlightType.BORDER);
		}
	}

}
