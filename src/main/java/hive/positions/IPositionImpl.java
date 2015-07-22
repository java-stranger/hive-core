package hive.positions;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer;

public interface IPositionImpl extends IPosition {
	
	Piece move(Coordinate from, Coordinate to);
	
	void insert(Piece p, Coordinate at);
	
	Piece remove(Coordinate at);
	
	IPlayer.Color switchTurn();
}