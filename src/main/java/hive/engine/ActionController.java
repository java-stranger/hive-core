package hive.engine;

import hive.actions.Action;
import hive.actions.Action.ActionType;
import hive.pieces.Piece;
import hive.positions.IPosition;
import hive.view.Hand;
import hive.view.Table;

public class ActionController {

	private Action curAction = null;
	final IPosition position;
	final Table view;
	final IController controller;
	
	ActionController(IController controller, Table view, IPosition position) {
		this.position = position;
		this.view = view;
		this.controller = controller;
	}
	
	void initiate(Action.ActionType type, Coordinate start, Piece p) {
		curAction = Action.create(type, start, p);
		System.out.println("Action created:" + curAction);
	}

	public void onClick(Coordinate c) {
		if(endAction(c)) {
			return;
		}
		
		Piece p = position.getTopPieceAt(c);
		if(p != null) {
			view.onPieceChoosenInField(p, c);
			initiate(ActionType.MOVE, c, p);
		} else {
			for(Hand h : view.hands.values()) {
				p = h.getPieceAt(c); 
				if(p != null)
					break;
			}
			if(p != null) {
				view.onPieceChoosenInHand(p, c);
				initiate(ActionType.INSERT, c, p);
			}
		}
		if(p == null)
			view.clearSelection();
	}

	public boolean endAction(Coordinate c) {
		if(curAction != null && curAction.check(position, c)) {
			controller.makeMove(curAction.createMove(c));
			curAction = null;
			return true;
		} else {
			System.out.println("Illegal action:" + curAction + " end @" + c);			
		}
		return false;
	}

}
