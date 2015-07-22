package hive.positions;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public interface IPositionImpl extends Serializable {
	
	Piece move(Coordinate from, Coordinate to);
	
	void insert(Piece p, Coordinate at);
	
	Piece remove(Coordinate at);

	void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer);

	void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer);

	boolean forEachVisibleWhileTrue(BiFunction<? super Coordinate, ? super Piece, Boolean> consumer);

	int getNumVisiblePieces();

	void reset();

	Piece getPieceAt(Coordinate pos, int i);

	boolean isFree(Coordinate pos);
	
	public Coordinate getAny();

}