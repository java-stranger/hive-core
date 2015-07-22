package test.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.pieces.PieceType;
import hive.player.IPlayer;
import hive.player.IPlayer.Color;
import hive.positions.Move;
import hive.positions.Position;

public class TestSerialization {
	
	static Object readObject(byte[] stream) throws ClassNotFoundException, IOException {
		return new ObjectInputStream(new ByteArrayInputStream(stream)).readObject();
	}

	static byte[] writeObject(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(obj);
		return bos.toByteArray();
	}

	@Test
	public void testCoordinate() {
		try {
			Coordinate origin = Coordinate.axial(0, 0);
			byte[] stream = writeObject(origin);
			assertEquals(origin, readObject(stream));

			stream = writeObject(Coordinate.axial(1, 0));
			assertEquals(Coordinate.axial(1, 0), readObject(stream));

			stream = writeObject(Coordinate.axial(1, -1));
			assertNotEquals(Coordinate.axial(0, -1), readObject(stream));
		}
		catch (Exception e) {
			fail("Failed with: " + e);
		}
	}

	@Test
	public void testPiece() {
		try {
			byte[] stream;

			stream = writeObject(Piece.createNew(IPlayer.Color.BLACK, PieceType.ANT));
			assertEquals(Piece.createNew(IPlayer.Color.BLACK, PieceType.ANT), readObject(stream));

			stream = writeObject(Piece.createNew(IPlayer.Color.WHITE, PieceType.BUG));
			assertEquals(Piece.createNew(IPlayer.Color.WHITE, PieceType.BUG), readObject(stream));
		}
		catch (Exception e) {
			fail("Failed with: " + e);
		}
	}

	@Test
	public void testMove() {
		try {
			byte[] stream;
			Piece p = Piece.createNew(IPlayer.Color.WHITE, PieceType.QUEEN);
			stream = writeObject(new Move(p, Coordinate.axial(0, 0), Coordinate.axial(1, 0)));
			assertEquals(new Move(p, Coordinate.axial(0, 0), Coordinate.axial(1, 0)), readObject(stream));

			p = Piece.createNew(IPlayer.Color.BLACK, PieceType.GRASSHOPER);
			stream = writeObject(new Move(p, null, Coordinate.axial(-1, 5)));
			assertEquals(new Move(p, null, Coordinate.axial(-1, 5)), readObject(stream));
		}
		catch (Exception e) {
			fail("Failed with: " + e);
		}
	}


	@Test
	public void testPosition() throws IOException, ClassNotFoundException {
		byte[] stream;
		Position pos = new Position();
		stream = writeObject(new Position());
		assertEquals(pos, readObject(stream));
		
		pos.accept(new Move(Piece.createNew(IPlayer.Color.WHITE, PieceType.ANT), null, Coordinate.axial(0, 0)));
		pos.accept(new Move(Piece.createNew(IPlayer.Color.BLACK, PieceType.BUG), null, Coordinate.axial(1, 0)));
		pos.accept(new Move(pos.getTopPieceAt(Coordinate.axial(0, 0)), Coordinate.axial(0, 0), Coordinate.axial(1, 1)));
		pos.accept(new Move(Piece.createNew(IPlayer.Color.BLACK, PieceType.QUEEN), null, Coordinate.axial(2, -1)));
		pos.accept(new Move(Piece.createNew(IPlayer.Color.WHITE, PieceType.BUG), null, Coordinate.axial(0, 2)));

		stream = writeObject(pos);
		assertEquals(pos, readObject(stream));
}

}
