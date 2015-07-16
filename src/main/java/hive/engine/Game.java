package hive.engine;

import java.util.ArrayList;
import java.util.HashMap;

import hive.pieces.Piece;
import hive.view.Hand;
import hive.view.Table;

public class Game implements Controller {
	
	public final HashMap<Player.Color, Player> players = new HashMap<>();
	
	public Position position = new Position();
	
	public Table view;
	
	private final ArrayList<Move> history = new ArrayList<>();
	
	private Coordinate selected = null;
	
	public Game() {
		for(Player.Color c : Player.Color.values()) {
			players.put(c, new hive.test.Player(c));
		}
		view = new Table(this);
	}

	public void start() {
		reset();
	}
	
	public void reset() {
		clearSelection();
		players.values().forEach((Player pl) -> pl.reset());
		position.reset();
		view.reset();
	}
	
	public void test() {
		
		int num_moves = 0;
		
		while(true) {
			nextMove();
			
			if(++num_moves > 2)
				break;
		}
	}
	
	public Player nextPlayer() {
		return players.get(position.next_turn);
	}
	
	public void nextMove() {
		Move move = nextPlayer().nextMove(position);
		
		if(move == null) {
			System.out.println("Game over: player " + nextPlayer() + " cannot move!");
			return;
		}
		
		makeMove(move);
	}
	
	public void makeMove(Move move) {
		position.accept(move);
		history.add(move);
		clearSelection();		
	}
	
	public void clearSelection() {
		selected = null;
		view.field.showPossibleMoves(null);
		view.field.showAdversaryMoves(null);
		view.field.setSelected(null);
	}
	
	public void undoLastMove() {
		if(!history.isEmpty()) {
			Move m = history.remove(history.size() - 1);
			position.undo(m);
			players.get(m.piece.color()).newPiece(m.piece);
			clearSelection();
		}
	}

	public void onPieceChoosenInHand(Piece p, Coordinate c) {
		selected = c;
		if(p.color() == nextPlayer().color()) {
			System.out.println("Showing possible moves (insertion)");			
			view.field.showPossibleMoves(PositionUtils.getPossibleInsertionCells(position, nextPlayer().color()));
		} else {
			System.out.println("Showing adversary possible moves (insertion)");			
			view.field.showAdversaryMoves(PositionUtils.getPossibleInsertionCells(position, p.color()));
		}
		view.field.setSelected(c);
	}

	public void onPieceChoosenInField(Piece p, Coordinate c) {
		selected = c;
		if(p.color() == position.next_turn) {
			System.out.println("Showing possible moves");			
			view.field.showPossibleMoves(p.getPossibleMoves(position, c));
		} else {
			System.out.println("Showing adversary possible moves");			
			view.field.showAdversaryMoves(p.getPossibleMoves(position, c));
		}
		view.field.setSelected(c);
	}
	
	public void onClick(Coordinate c) {
		Piece p = position.getTopPieceAt(c);
		if(p != null) {
			onPieceChoosenInField(p, c);
		} else {
			for(Hand h : view.hands.values()) {
				p = h.getPieceAt(c); 
				if(p != null)
					break;
			}
			if(p != null)
				onPieceChoosenInHand(p, c);
		}
		if(p == null)
			clearSelection();

//		Piece p = position.getTopPieceAt(selected);
//		if(p != null && p.color() == nextPlayer().color())
//		{
//			if(nextPlayer().remaining.contains(selected) 
//					&& PositionUtils.getPossibleInsertionCells(position, p.color()).contains(c)) {
//				// make this move!
//				nextPlayer().remaining.remove(selected);
//				makeMove(new Move(p, null, c));
//			} else if(selected != null 
//					&& p.getPossibleMoves(position, c).contains(c)) {
//				makeMove(new Move(p, selected, c));
//			} else {
//				System.out.println("Moving " + selected + " to " + c + " is not allowed!");			
//			}
//		} else {
//		}
	}
	
	public void savePosition(String filename) {
		
	}

}
