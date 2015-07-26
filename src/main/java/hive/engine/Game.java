package hive.engine;

import java.util.HashMap;
import java.util.List;

import hive.player.IPlayer;
import hive.positions.CheckedPosition;
import hive.positions.IPlayable;
import hive.positions.IPosition;
import hive.positions.ViewablePosition;
import hive.view.PlayingField;

public class Game {
	
	public final HashMap<IPlayer.Color, IPlayer> players = new HashMap<>();
	
	protected final ViewablePosition viewablePosition = new ViewablePosition();
	
	protected final IPlayable position = new CheckedPosition(viewablePosition);
	
	protected final MoveHistory moveHistory = new MoveHistory();
	
	public Game() {
		for(IPlayer.Color c : IPlayer.Color.values()) {
			players.put(c, new hive.test.Player(c));
		}
	}

	public void subscribeView(PlayingField view) {
		viewablePosition.subscribeView(view);
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
		moveHistory.accept(move);
		fastforwardMove();
	}
	
	public void fastforwardMove() {
		Move move = moveHistory.replayMove();
		System.out.println("Make move: " + move);
		position.accept(move);
		players.values().forEach((IPlayer pl) -> pl.notify(move));
	}
	
	private void rollbackMove() {
		Move m = moveHistory.rollback(1);
		System.out.println("Undo move: " + m);
		position.undo(m);
		players.values().forEach((IPlayer pl) -> pl.notifyUndo(m));
	}
	
	public void undoLastMove() {
		if(!moveHistory.isEmpty()) {
			rollbackMove();
		}
	}
	
	public void goToNthMove(int n) {
		if(n < 0 || n > moveHistory.size()) {
			throw new IllegalArgumentException("Asked to go to " +n+ "th move, but only have " + moveHistory.size());
		}
		int to_rollback = moveHistory.current() - n;
		if(to_rollback >= 0) {
			for(int i = 0; i < to_rollback; ++i) {
				rollbackMove();
			}
		} else {			
			for(int i = 0; i < -to_rollback; ++i) {
				fastforwardMove();
			}
		}
	}

	public void registerMoveHistoryListeners(List<IMoveHistoryListener> listeners) {
		moveHistory.registerMoveHistoryListeners(listeners);
	}
}
