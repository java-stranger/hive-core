package hive.positions;

import hive.engine.Move;

/**
 * @author mac
 *
 */
public final class CheckedPosition extends SimplePosition {

	private static final long serialVersionUID = -8510714262592423472L;

	protected final IPositionChecker positionChecker = new PositionChecker();
	
	public CheckedPosition() {
		this(new PositionImpl());
	}

	public CheckedPosition(IPositionImpl impl) {
		super(impl);
	}

	@Override
	public CheckedPosition accept(Move m) {
		positionChecker.checkMove(this, m, nextTurn());
		
		super.accept(m);
		
		try {
			positionChecker.checkPositionInvariants(this);
		} catch (Throwable exc) {
			super.undo(m); // best-effort exception safety - try to rollback
			throw exc;
		}
		
		return this;
	}

	@Override
	public CheckedPosition undo(Move m) {
		positionChecker.checkUndo(this, m, nextTurn());
		
		super.undo(m);

		try {
			positionChecker.checkPositionInvariants(this);			
		} catch (Throwable exc) {
			super.accept(m); // best-effort exception safety - try to rollback
			throw exc;
		}

		return this;
	}


}
