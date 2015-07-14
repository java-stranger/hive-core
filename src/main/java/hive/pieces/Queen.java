package main.java.hive.pieces;

import java.util.HashSet;

import main.java.hive.engine.Coordinate;
import main.java.hive.engine.Player;
import main.java.hive.engine.Position;

public class Queen extends Piece {
	Queen(Player.Color color) {
		super(color, PieceType.QUEEN);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this))
			return board.getAccesibleNeighboringCells(pos);
		else 
			return empty;
	}

}
