package hive.engine;

import java.util.ArrayList;
import java.util.HashMap;

import hive.pieces.Piece;
import hive.view.Table;

public class Game implements Controller {
	
	public final HashMap<Player.Color, Player> players = new HashMap<>();
	
	public Position position = new Position();
	
	public Table view;
	
	private final ArrayList<Move> history = new ArrayList<>();
	
	private Piece selected = null;
	
	public Game() {
		for(Player.Color c : Player.Color.values()) {
			players.put(c, new hive.test.Player(c));
		}
		view = new Table(this);
	}

	public void start() {
//		reset();
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
		refreshSelection();		
	}
	
	public void clearSelection() {
		selected = null;
		view.field.showPossibleMoves(null);
		view.field.showAdversaryMoves(null);
	}
	
	public void refreshSelection() {
		if(selected != null) {
			if(players.get(selected.color()).remaining.contains(selected)) {
				onPieceChoosenInHand(selected);
			} else {
				onPieceChoosenInField(selected);
			}
		}
	}
	public void undoLastMove() {
		if(!history.isEmpty()) {
			Move m = history.remove(history.size() - 1);
			position.undo(m);
			players.get(m.piece.color()).newPiece(m.piece);
			refreshSelection();
		}
	}

	public void onPieceChoosenInHand(Piece p) {
		selected = p;
		if(p.color() == nextPlayer().color()) {
			System.out.println("Showing possible moves");			
			view.field.showPossibleMoves(position.getPossibleInsertionCells(p.color()));
		} else {
			view.field.showAdversaryMoves(position.getPossibleInsertionCells(p.color()));
		}
	}

	public void onPieceChoosenInField(Piece p) {
		selected = p;
		if(p.color() == position.next_turn) {
			System.out.println("Showing possible moves");			
			view.field.showPossibleMoves(p.getPossibleMoves(position));
		} else {
			System.out.println("Showing adversary possible moves");			
			view.field.showAdversaryMoves(p.getPossibleMoves(position));
		}
	}
	
	public void onClick(Coordinate c) {
		if(selected != null && selected.color() == nextPlayer().color())
		{
			if(nextPlayer().remaining.contains(selected) 
					&& position.getPossibleInsertionCells(selected.color()).contains(c)) {
				// make this move!
				nextPlayer().remaining.remove(selected);
				makeMove(new Move(selected, null, c));
			} else if(position.getCoordinate(selected) != null 
					&& selected.getPossibleMoves(position).contains(c)) {
				makeMove(new Move(selected, position.getCoordinate(selected), c));
			} else {
				System.out.println("Moving " + selected + " to " + c + " is not allowed!");			
			}
		} else {
		}
	}

}
