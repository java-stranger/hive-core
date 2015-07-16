package test.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hive.engine.Coordinate;
import hive.engine.Move;
import hive.engine.Position;
import hive.engine.Player.Color;
import hive.pieces.Piece;
import hive.pieces.PieceType;

public class TestUtils {
	public static <T> Set<T> toSet(T[] array) {
		return new HashSet<>(Arrays.asList(array));
	}
	
	public static Position position1() {
		Position pos = new Position();
		
		pos.accept(new Move(Piece.createNew(Color.WHITE, PieceType.ANT), null, Coordinate.axial(0, 0)));
		pos.accept(new Move(Piece.createNew(Color.BLACK, PieceType.BUG), null, Coordinate.axial(1, 0)));
		pos.accept(new Move(Piece.createNew(Color.WHITE, PieceType.QUEEN), null, Coordinate.axial(0, -1)));
		pos.accept(new Move(Piece.createNew(Color.BLACK, PieceType.QUEEN), null, Coordinate.axial(2, -1)));
		pos.accept(new Move(Piece.createNew(Color.WHITE, PieceType.ANT), null, Coordinate.axial(-1, 1)));
		pos.accept(new Move(Piece.createNew(Color.BLACK, PieceType.SPIDER), null, Coordinate.axial(2, -2)));

		return pos;
	}
}
