package hive.test;

import java.util.ArrayList;
import java.util.Random;

import hive.engine.Coordinate;
import hive.engine.Move;
import hive.pieces.Piece;
import hive.player.AbstractPlayer;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;
import hive.positions.SimplePosition;

public class Player extends AbstractPlayer {
	
	Random rand = new Random(1234567876531L);
	
	SimplePosition position = new SimplePosition();
	
	protected AbstractPlayer otherPlayer;

	private int numMoves = 0; 

	public Player(IPlayer.Color color) {
		super(color);
		otherPlayer = new AbstractPlayer(color.next()) {
			@Override
			public Move nextMove(IPosition board) {
				return null;
			}
		};
	}
	
	public void reset() {
		super.reset();
		otherPlayer.reset();
		position = new SimplePosition();
	}
	
	public void notify(Move move) {
		position.accept(move);
		try {
			super.notify(move);  // only one of the two can throw, normally
			otherPlayer.notify(move);
		} catch (Throwable e) {
			position.undo(move);  // Can throw as well... Then we lose e, and everything...
			throw e;
		}
	}

	public void notifyUndo(Move move) {
		position.undo(move);
		try {
			super.notifyUndo(move); // only one of the two can throw, normally
			otherPlayer.notifyUndo(move);
		} catch (Throwable e) {
			position.accept(move); // Can throw as well... Then we lose e, and everything...
			throw e;
		}
	}

	
	public ArrayList<Move> getMoves(IPosition board) {
		
		ArrayList<Move> moves = new ArrayList<>();

		board.forEachVisible((Coordinate at, Piece p) -> {
			if(p.color() == board.nextTurn() && PositionUtils.canMove(board, p, at)) {
				for(Coordinate to : p.getPossibleMoves(board, at)) {
					moves.add(new Move(p, at, to));
				}
			}
		});
		
		IPlayer player = board.nextTurn() == this.color() ? this : otherPlayer;
		
		player.remaining((Piece p, Integer num) -> {
			PositionUtils.getPossibleInsertionCells(board, this.color()).forEach((Coordinate to) -> {
				moves.add(new Move(p, null, to));
			});
		});
		
		return moves;
	}

	public Move nextMove(IPosition board) {
		assert position.equals(board);
		
		long started = System.currentTimeMillis(); 
		numMoves = 1;
		
		Metric metric = evaluate(position, 3);
		
		long took = (System.currentTimeMillis() - started);
		System.out.println("Best sequence: " + metric);
		System.out.println("Analyzed " + numMoves + " positions in " + took + "ms (" + (1000 * took / numMoves) + "us/position)");
		
		return metric.moves.get(0);
	}
	
	Metric evaluate(IPosition board, int max_depth) {
		Metric m = new Metric();
		ArrayList<Move> moves = getMoves(board);

		if(board.nextTurn() == color()) {
			m.numPiecesMoveableMe = moves.size();
		} else {
			m.numPiecesMoveableAdv = moves.size();
		}

		if(max_depth <= 1) {
//			System.out.println("Reached max depth, metric " + m);
			++numMoves;
			return m;
		}
		
		Move best_move = null;
		
//		System.out.println(max_depth + ": Possible moves: " + moves.get(board.nextTurn()));

		for(Move move : moves) {
//			System.out.println(max_depth + ": Checking move " + move);
			notify(move);
			Metric metric = evaluate(board, max_depth - 1);
			notifyUndo(move);
			if(best_move == null || metric.isBetter(m)) {
				best_move = move;
				m = metric;
			}
		};
		
		return m.addFront(best_move);
	}

}
