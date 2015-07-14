package main.java.hive.pieces;

import java.util.HashSet;

import main.java.hive.engine.Coordinate;
import main.java.hive.engine.Player;
import main.java.hive.engine.Position;

public class Ant extends Piece {

	public Ant(Player.Color color) {
		super(color, PieceType.ANT);
	}

	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this)) {
			return board.getAccesibleCellsRamping(pos);
		}
		else 
			return empty;
	}

}
