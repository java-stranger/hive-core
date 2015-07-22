package hive.player;

import java.util.function.BiConsumer;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.positions.IPosition;
import hive.positions.Move;

public interface IPlayer {

	enum Color {
		WHITE { @Override public Color next() { return BLACK;} },
		BLACK { @Override public Color next() { return WHITE;} };
		
		abstract public Color next();
	}

	void reset();

	void remaining(BiConsumer<? super Piece, Integer> consumer);

	void notify(Move move);

	void notifyUndo(Move move);

	IPlayer.Color color();

	Move nextMove(IPosition board);

}