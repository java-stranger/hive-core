package hive.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MoveHistory {
	
	public boolean isEmpty() {
		return history.isEmpty();
	}

	private final ArrayList<Move> history = new ArrayList<>();
	private int current_move = 0;
	
	private List<IMoveHistoryListener> listeners = new ArrayList<>();

	public void registerMoveHistoryListeners(List<IMoveHistoryListener> listeners) {
		this.listeners = listeners;
		listeners.forEach((IMoveHistoryListener listener) -> listener.listUpdated(history));
	}
	
	void accept(Move move) {
		if(current_move >= history.size() || !move.equals(history.get(current_move))) {
			// truncate the "future" history up to the current move
			boolean truncated = false;
			ListIterator<Move> it = history.listIterator(current_move);
			while(it.hasNext()) {
				it.next();
				it.remove();			
				truncated = true;
			}
			history.add(current_move, move);
			
			if(truncated) {
				listeners.forEach((IMoveHistoryListener listener) -> listener.listUpdated(history));
			} else {
				listeners.forEach((IMoveHistoryListener listener) -> listener.moveAdded(move));
			}
		}
//		++current_move;
	}

	Move rollback(int how_many) {
		assert how_many <= current_move;
		current_move -= how_many;
		listeners.forEach((IMoveHistoryListener listener) -> listener.selectionChanged(current_move));
		return history.get(current_move);
	}

	public ArrayList<Move> getMoves() {
		return (ArrayList<Move>) history.clone(); // defensive copying
	}

	public int size() {
		return history.size();
	}
	
	public int current() {
		return current_move;
	}

	public Move replayMove() {
		listeners.forEach((IMoveHistoryListener listener) -> listener.selectionChanged(current_move + 1));
		return history.get(current_move++);
	}

}
