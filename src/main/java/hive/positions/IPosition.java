package hive.positions;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer;

public interface IPosition extends Serializable {

	public Piece getTopPieceAt(Coordinate pos);

	public Piece getBelowPieceAt(Coordinate pos, int i);

	public boolean isFree(Coordinate pos);

	public int numTopPieces();
	
	public IPlayer.Color nextTurn();
	
	public void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer);
	
	public void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer);
	
	public Coordinate getStartingPoint();
}
