package hive.positions;

import java.io.Serializable;

import hive.engine.Move;

/**
 * @author mac
 *
 */
public class SimplePosition extends ForwardPosition implements Serializable, IPlayable {

	private static final long serialVersionUID = -5065051856963002090L;

	public SimplePosition(IPositionImpl impl) {
		super(impl);
	}
	
	public SimplePosition() {
		this(new PositionImpl());
	}
	
	public SimplePosition accept(Move m) {
		assert nextTurn() == m.piece.color();
		
		if(m.from == null) {
			insert(m.piece, m.to);
//			System.out.println(m.piece + " is inserted at " + m.to);
		} else {
			move(m.from, m.to);
//			System.out.println(m.piece + " is moved from " + m.from + " to " + m.to);
		}
		super.switchTurn();
		
		return this;
	}
	
	public SimplePosition undo(Move m) {
		assert nextTurn() != m.piece.color(); // can only undo the last move

		if(m.from == null) {
			remove(m.to);
//			System.out.println(m.piece + " is removed at " + m.to);
		} else {
			move(m.to, m.from);
//			System.out.println(m.piece + " is moved back from " + m.to + " to " + m.from);
		}
		super.switchTurn();
		
		return this;
	}


}
