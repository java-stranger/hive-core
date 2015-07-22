package hive.engine;

public interface IController {

	void makeMove(Move move);

	void undoLastMove();

	void savePosition(String filename);

	void loadPosition(String filename);

	void displayBorder();
	
	void newGame();

	void nextMove();

	void onClick(Coordinate c);

}