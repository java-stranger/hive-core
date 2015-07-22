package hive.positions;

import java.util.function.BiConsumer;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer;

public interface IPosition {

	public Piece getTopPieceAt(Coordinate pos);

	public Piece getPieceAt(Coordinate pos, int below);

	public boolean isFree(Coordinate pos);

	public int numTopPieces();
	
	public IPlayer.Color nextTurn();
	
	public void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer);
	
	public void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer);
	
	public Coordinate getAnyPoint();
}
