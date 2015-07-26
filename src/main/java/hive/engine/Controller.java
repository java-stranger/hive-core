package hive.engine;

import java.util.ArrayList;
import java.util.List;

import hive.view.Table;

public abstract class Controller implements IController {

	Table view;
	Game game;
	
	ActionController actionCtrl;
	
	public void setGameAndView(Game game, Table view) {
		this.game = game;
		this.view = view;
		actionCtrl = new ActionController(this, view, game.position());
		game.registerMoveHistoryListeners(moveListeners);
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
	
	public void onMoveSelectedFromHistory(int selectionIndex) {
		game.goToNthMove(selectionIndex);
	}
	
	List<IMoveHistoryListener> moveListeners = new ArrayList<>();
	public void addMoveHistoryListener(IMoveHistoryListener listener) {
		moveListeners.add(listener);
	}


}
