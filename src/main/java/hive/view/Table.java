package hive.view;

import java.util.HashMap;

import hive.actions.Action;
import hive.actions.Action.ActionType;
import hive.engine.Coordinate;
import hive.engine.Game;
import hive.pieces.Piece;
import hive.player.IPlayer;
import hive.player.IPlayer.Color;
import hive.positions.PositionUtils;

public class Table {
	
	private final int w = 14;
	private final int h = 9;
	public final HashMap<IPlayer.Color, Hand> hands = new HashMap<>();
	public final PlayingField field;
	
	public final Game game;
	
	private Action curAction = null;
	
	public Table(Game game) {
		this.game = game;
		field = new PlayingField(game, w, h, hands);

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
		
		hands.put(IPlayer.Color.BLACK, new Hand(Coordinate.axial(topLeftX, topLeftY), false));
		hands.put(IPlayer.Color.WHITE, new Hand(Coordinate.axial(topLeftX, topLeftY), true));
		
		for(IPlayer.Color color : game.players.keySet()) {
			IPlayer pl = game.players.get(color);
			pl.remaining((Piece p, Integer num) -> {
				for(int i = 0; i < num; ++i)
					field.insertPiece(p, hands.get(color).placeInHand(p));
			});
		}
	}

	public void reset() {
		field.reset();
		initHands();
		clearSelection();
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
			field.showPossibleMoves(PositionUtils.getPossibleInsertionCells(game.position, game.nextPlayer().color()));
		} else {
			System.out.println("Showing adversary possible moves (insertion)");			
			field.showAdversaryMoves(PositionUtils.getPossibleInsertionCells(game.position, p.color()));
		}
		field.setSelected(c);
	}

	public void onPieceChoosenInField(Piece p, Coordinate c) {
		if(p.color() == game.nextPlayer().color()) {
			System.out.println("Showing possible moves");			
			field.showPossibleMoves(p.getPossibleMoves(game.position, c));
		} else {
			System.out.println("Showing adversary possible moves");			
			field.showAdversaryMoves(p.getPossibleMoves(game.position, c));
		}
		field.setSelected(c);
	}
	
	public void clearSelection() {
		field.showPossibleMoves(null);
		field.showAdversaryMoves(null);
		field.setSelected(null);
	}

	public boolean endAction(Coordinate c) {
		if(curAction != null && curAction.check(game, c)) {
			game.makeMove(curAction.createMove(c));
			curAction = null;
			return true;
		} else {
			System.out.println("Illegal action:" + curAction + " end @" + c);			
		}
		return false;
	}

	public void onClick(Coordinate c) {
		if(endAction(c)) {
			return;
		}
		
		Piece p = game.position.getTopPieceAt(c);
		if(p != null) {
			onPieceChoosenInField(p, c);
			curAction = Action.create(ActionType.MOVE, c, p);
			System.out.println("Action created:" + curAction);
		} else {
			for(Hand h : hands.values()) {
				p = h.getPieceAt(c); 
				if(p != null)
					break;
			}
			if(p != null) {
				onPieceChoosenInHand(p, c);
				curAction = Action.create(ActionType.INSERT, c, p);
				System.out.println("Action created:" + curAction);
			}
		}
		if(p == null)
			clearSelection();
	}
	
	public void displayExternalBorder() {
		field.displayExternalBorder(PositionUtils.getExternalBorder(game.position));
	}

}
