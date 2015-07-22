package hive.engine;

import hive.actions.Action;
import hive.actions.Action.ActionType;
import hive.pieces.Piece;
import hive.view.Hand;
import hive.view.Table;

public abstract class Controller implements IController {

	final Table view;
	final Game game;
	
	final ActionController actionCtrl;
	
	public Controller(Game game, Table view) {
		this.game = game;
		this.view = view;
		actionCtrl = new ActionController(this, view, game.position());
	}
	
	/* (non-Javadoc)
	 * @see hive.engine.IController#makeMove(hive.engine.Move)
	 */
	public void makeMove(Move move) {
		System.out.println("Make move: " + move);
		game.makeMove(move);
		view.clearSelection();		
	}
	
	/* (non-Javadoc)
	 * @see hive.engine.IController#undoLastMove()
	 */
	public void undoLastMove() {
		game.undoLastMove();
		view.clearSelection();		
	}
	
	/* (non-Javadoc)
	 * @see hive.engine.IController#savePosition(java.lang.String)
	 */
	public void savePosition(String filename) {
		System.out.println("Saving to " + filename);
	}

	/* (non-Javadoc)
	 * @see hive.engine.IController#loadPosition(java.lang.String)
	 */
	public void loadPosition(String filename) {
		System.out.println("Loading from " + filename);
	}

	/* (non-Javadoc)
	 * @see hive.engine.IController#displayBorder()
	 */
	public void displayBorder() {
		view.displayExternalBorder();
	}

	@Override
	public void nextMove() {
		game.nextMove();
	}
	
	public void onClick(Coordinate c) {
		actionCtrl.onClick(c);
	}

}
