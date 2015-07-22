package hive.positions;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer;
import hive.view.PlayingField;

/**
 * @author mac
 *
 */
public final class Position implements Serializable, IPosition, GamePosition {
	
	private static final long serialVersionUID = 6740990797043611124L;
	
	protected IPlayer.Color next_turn = IPlayer.Color.WHITE;
	protected PlayingField view = null;

	protected final IPositionImpl positionImpl;
	protected final IPositionChecker positionChecker;
	
	public Position() {
		this(new PositionTraits());
	}

	public Position(PositionTraits traits) {
		positionImpl = traits.createPositionImpl();
		positionChecker = traits.createPositionChecker();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position)) {
			return false;
		}
		Position pos = (Position) obj;
		return this.next_turn == pos.next_turn 
				&& this.positionImpl.equals(pos.positionImpl);
	}
	
	@Override
	public String toString() {		
		return positionImpl.toString() + ", " + next_turn + "'s turn";
	}
	
	public PlayingField subscribeView(PlayingField view) {
		PlayingField temp = this.view;
		this.view = view;
		return temp;
	}
	
	public void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		positionImpl.forEach(consumer);
	}
	
	public void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		positionImpl.forEachVisible(consumer);
	}
	
	public boolean forEachVisibleWhileTrue(BiFunction<? super Coordinate, ? super Piece, Boolean> consumer) {
		return positionImpl.forEachVisibleWhileTrue(consumer);
	}
	
	/**
	 * @return Number of visible (top-level) pieces. Might be different from the total number
	 * of pieces (if some of them are hidden by bugs)
	 */
	public int numTopPieces() {
		return positionImpl.getNumVisiblePieces();
	}
	
	public IPlayer.Color nextTurn() {
		return next_turn;
	}
	
	public void reset() {
		if(view != null) forEach((Coordinate c, Piece p) -> view.removePiece(p, c));
		positionImpl.reset();
		next_turn = IPlayer.Color.WHITE;
	}

	protected void move(Piece p, Coordinate from, Coordinate to) {
		Piece moved = positionImpl.move(from, to);
		try {
			assert p.equals(moved);
			if(view != null) view.movePiece(p, from, to);
		} catch (Throwable exc) {
			positionImpl.move(to, from); // rollback!
			throw exc;
		}
	}

	protected void insert(Piece p, Coordinate at) {
		positionImpl.insert(p, at);
		try {
			if(view != null) view.playPiece(p, at);
		} catch (Throwable exc) {
			positionImpl.remove(at); // rollback!
			throw exc;
		}
	}
	
	protected void remove(Piece p, Coordinate at) {
		Piece removed = positionImpl.remove(at);
		try {
			assert removed.equals(p);
			if(view != null) view.removePiece(p, at);
		} catch (Throwable exc) {
			positionImpl.insert(removed, at); // rollback!
			throw exc;
		}
	}
	
	public Piece getTopPieceAt(Coordinate pos) {
		return positionImpl.getPieceAt(pos, 0);
	}

	public Piece getBelowPieceAt(Coordinate pos, int i) {
		return positionImpl.getPieceAt(pos, i);
	}

	public boolean isFree(Coordinate pos) {
		return positionImpl.isFree(pos);
	}
	
	public Coordinate getStartingPoint() {
		return positionImpl.getAny();
	}
	
	Piece virtuallyRemove(Coordinate at) {
		return positionImpl.remove(at);
	}

	void virtuallyPlaceBack(Piece p, Coordinate at) {
		positionImpl.insert(p, at);
	}
	
	public Position accept(Move m) {
		assert next_turn == m.piece.color();
		
		positionChecker.checkMove(this, m, next_turn);
		
		if(m.from == null) {
			insert(m.piece, m.to);
//			System.out.println(m.piece + " is inserted at " + m.to);
		} else {
			move(m.piece, m.from, m.to);
//			System.out.println(m.piece + " is moved from " + m.from + " to " + m.to);
		}
		next_turn = next_turn.next();
		
		try {
			positionChecker.checkPositionInvariants(this);
		} catch (Throwable exc) {
			undo(m); // best-effort exception safety - try to rollback
			throw exc;
		}
		
		return this;
	}
	
	public Position undo(Move m) {
		assert next_turn != m.piece.color(); // can only undo the last move

		positionChecker.checkUndo(this, m, next_turn);

		if(m.from == null) {
			remove(m.piece, m.to);
//			System.out.println(m.piece + " is removed at " + m.to);
		} else {
			move(m.piece, m.to, m.from);
//			System.out.println(m.piece + " is moved back from " + m.to + " to " + m.from);
		}
		next_turn = next_turn.next();
		
		try {
			positionChecker.checkPositionInvariants(this);			
		} catch (Throwable exc) {
			accept(m); // best-effort exception safety - try to rollback
			throw exc;
		}

		return this;
	}


}
