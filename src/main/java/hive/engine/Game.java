package hive.engine;

import java.util.ArrayList;
import java.util.HashMap;

import hive.player.IPlayer;
import hive.positions.CheckedPosition;
import hive.positions.IPlayable;
import hive.positions.IPosition;
import hive.positions.ViewablePosition;
import hive.test.Player;

public class Game {
	
	public final HashMap<IPlayer.Color, Player> players = new HashMap<>();
	
	public final ViewablePosition viewablePosition = new ViewablePosition();
	
	public final IPlayable position = new CheckedPosition(viewablePosition);
	
	private final ArrayList<Move> history = new ArrayList<>();
	
	public Game() {
		for(IPlayer.Color c : IPlayer.Color.values()) {
			players.put(c, new hive.test.Player(c));
		}
	}

	public void start() {
	}
	
	public void test() {
		
		int num_moves = 0;
		
		while(true) {
			nextMove();
			
			if(++num_moves > 2)
				break;
		}
	}
	
	public IPosition position() {
		return position;
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
	}
	
	public void undoLastMove() {
		if(!history.isEmpty()) {
			Move m = history.remove(history.size() - 1);
			System.out.println("Undo move: " + m);
			position.undo(m);
			players.values().forEach((IPlayer pl) -> pl.notifyUndo(m));
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
	
}
