package hive.view;

import java.util.HashMap;

import hive.actions.Action;
import hive.actions.Action.ActionType;
import hive.engine.Coordinate;
import hive.engine.Game;
import hive.pieces.Piece;
import hive.player.IPlayer;
import hive.positions.PositionUtils;

public class Table {
	
	private final int w = 14;
	private final int h = 9;
	public final HashMap<IPlayer.Color, Hand> hands = new HashMap<>();
	public final PlayingField field = new PlayingField(w, h, hands);
	
	public Game game;
	
	public void setGame(Game game) {

		this.game = game;
		
		initHands();
		
		game.subscribeView(field);
	}
	
	private void initHands() {
		hands.clear();
		int topY = -h / 2;

		int topLeftX = 0 - w / 2;
		int topLeftY = topY + (w + 1) / 4;
		
//		int topRightX = -topLeftX;
//		int topRightY = topLeftY - w / 2;
		
		hands.put(IPlayer.Color.BLACK, new Hand(game.position(), Coordinate.axial(topLeftX, topLeftY), false));
		hands.put(IPlayer.Color.WHITE, new Hand(game.position(), Coordinate.axial(topLeftX, topLeftY), true));
		
		for(IPlayer.Color color : game.players.keySet()) {
			IPlayer pl = game.players.get(color);
			pl.remaining((Piece p, Integer num) -> {
				for(int i = 0; i < num; ++i)
					field.insertPiece(p, hands.get(color).placeInHand(p));
			});
		}
	}

	public Hand getPlayersHand(IPlayer.Color c) {
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
	
	public void onPieceChoosenInHand(Piece p, Coordinate c) {
		if(p.color() == game.nextPlayer().color()) {
			System.out.println("Showing possible moves (insertion)");			
			field.showPossibleMoves(PositionUtils.getPossibleInsertionCells(game.position(), game.nextPlayer().color()));
		} else {
			System.out.println("Showing adversary possible moves (insertion)");			
			field.showAdversaryMoves(PositionUtils.getPossibleInsertionCells(game.position(), p.color()));
		}
		field.setSelected(c);
	}

	public void onPieceChoosenInField(Piece p, Coordinate c) {
		if(p.color() == game.nextPlayer().color()) {
			System.out.println("Showing possible moves");			
			field.showPossibleMoves(p.getPossibleMoves(game.position(), c));
		} else {
			System.out.println("Showing adversary possible moves");			
			field.showAdversaryMoves(p.getPossibleMoves(game.position(), c));
		}
		field.setSelected(c);
	}
	
	public void clearSelection() {
		field.showPossibleMoves(null);
		field.showAdversaryMoves(null);
		field.setSelected(null);
	}

	
	public void displayExternalBorder() {
		field.displayExternalBorder(PositionUtils.getExternalBorder(game.position()));
	}

}
