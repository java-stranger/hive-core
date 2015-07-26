package hive.engine;

import java.util.List;

public interface IMoveHistoryListener {
	void moveAdded(Move move);

	void selectionChanged(int index);

	void listUpdated(List<Move> moves);
}
