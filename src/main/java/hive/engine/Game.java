package hive.engine;

import java.util.ArrayList;
import java.util.HashMap;

import hive.player.IPlayer;
import hive.positions.Position;
import hive.positions.CheckedPositionTraits;
import hive.positions.Move;
import hive.test.Player;
import hive.view.Table;

public class Game implements Controller {
	
	public final HashMap<IPlayer.Color, Player> players = new HashMap<>();
	
	public Position position = new Position(new CheckedPositionTraits());
	
	public Table view;
	
	private final ArrayList<Move> history = new ArrayList<>();
	
	public Game() {
		for(IPlayer.Color c : IPlayer.Color.values()) {
			players.put(c, new hive.test.Player(c));
		}
		view = new Table(this);
	}

	public void start() {
		reset();
	}
	
	public void reset() {
		players.values().forEach((IPlayer pl) -> pl.reset());
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
	
	public IPlayer nextPlayer() {
		return players.get(position.nextTurn());
	}
	
	public void nextMove() {
		IPlayer player = nextPlayer();
		Move move = player.nextMove(position);
		
		if(move == null) {
			System.out.println("Game over: player " + player + " cannot move!");
			return;
		}
		
		makeMove(move);
	}
	
	public void makeMove(Move move) {
		System.out.println("Make move: " + move);
		position.accept(move);
		history.add(move);
		players.values().forEach((IPlayer pl) -> pl.notify(move));
		view.clearSelection();		
	}
	
	public void undoLastMove() {
		if(!history.isEmpty()) {
			Move m = history.remove(history.size() - 1);
			System.out.println("Undo move: " + m);
			position.undo(m);
			players.values().forEach((IPlayer pl) -> pl.notifyUndo(m));
			view.clearSelection();
		}
	}

	
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
	
	public void savePosition(String filename) {
		System.out.println("Saving to " + filename);
	}

	public void loadPosition(String filename) {
		System.out.println("Loading from " + filename);
	}

	public void displayBorder() {
		view.displayExternalBorder();
	}

}
