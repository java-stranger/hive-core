package hive.view;

import java.util.HashMap;

import hive.engine.Coordinate;
import hive.engine.Game;
import hive.engine.Player;
import hive.engine.Player.Color;
import hive.pieces.Piece;

public class Table {
	
	private final int w = 14;
	private final int h = 9;
	public final HashMap<Player.Color, Hand> hands = new HashMap<>();
	public final PlayingField field = new PlayingField(w, h, hands);
	
	public final Game game; 
	
	public Table(Game game) {
		this.game = game;

		initHands();
		
		game.position.subscribeView(field);
	}
	
	private void initHands() {
		hands.clear();
		int topY = -h / 2;

		int topLeftX = 0 - w / 2;
		int topLeftY = topY + (w + 1) / 4;
		
//		int topRightX = -topLeftX;
//		int topRightY = topLeftY - w / 2;
		
		hands.put(Color.BLACK, new Hand(Coordinate.axial(topLeftX, topLeftY), false));
		hands.put(Color.WHITE, new Hand(Coordinate.axial(topLeftX, topLeftY), true));
		
		for(Color color : game.players.keySet()) {
			Player pl = game.players.get(color);
			pl.getPieces().forEach((Piece p) -> field.insertPiece(p, hands.get(color).allocate(p)));
		}
	}

	public void reset() {
		initHands();
	}

	public Hand getPlayersHand(Color c) {
		return hands.get(c);
	}
	
//	public void onTileSelected(Tile t) {
//		if(t == null) {
//			game.clearSelection();
//			return;
//		}
//		
//		// First, find out where the tile is selected
//		if(game.players.get(t.piece.color()).getPieces().contains(t.piece)) {
//			System.out.println("Choosen piece on the hand of: " + game.players.get(t.piece.color()));
//			game.onPieceChoosenInHand(t.pos);
//		} else {
//			System.out.println("Choosen piece on the field!");
//			game.onPieceChoosenInField(t.pos);
//		}		
//	}

	public void onClick(Coordinate c) {
		game.onClick(c);
	}
}
