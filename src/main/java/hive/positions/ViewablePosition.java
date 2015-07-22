package hive.positions;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.view.PlayingField;

/**
 * @author mac
 *
 */
public final class ViewablePosition extends ForwardPosition {
	
	protected PlayingField view = null;

	public ViewablePosition() {
		this(new PositionImpl());
	}

	public ViewablePosition(IPositionImpl impl) {
		super(impl);
	}

	public void subscribeView(PlayingField view) {
 		this.view = view;
	}

	@Override
	public Piece move(Coordinate from, Coordinate to) {
		Piece moved = super.move(from, to);
		try {
			if(view != null) view.movePiece(moved, from, to);
		} catch (Throwable exc) {
			super.move(to, from); // rollback!
			throw exc;
		}
		return moved;
	}

	@Override
	public void insert(Piece p, Coordinate at) {
		super.insert(p, at);
		try {
			if(view != null) view.playPiece(p, at);
		} catch (Throwable exc) {
			super.remove(at); // rollback!
			throw exc;
		}
	}
	
	@Override
	public Piece remove(Coordinate at) {
		Piece removed = super.remove(at);
		try {
			if(view != null) view.removePiece(removed, at);
		} catch (Throwable exc) {
			super.insert(removed, at); // rollback!
			throw exc;
		}
		return removed;
	}
}
