package hive.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import hive.engine.Player.Color;
import hive.pieces.Piece;
import hive.pieces.PieceType;

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

			stream = writeObject(Piece.createNew(Color.BLACK, PieceType.ANT));
			assertEquals(Piece.createNew(Color.BLACK, PieceType.ANT), readObject(stream));

			stream = writeObject(Piece.createNew(Color.WHITE, PieceType.BUG));
			assertEquals(Piece.createNew(Color.WHITE, PieceType.BUG), readObject(stream));
		}
		catch (Exception e) {
			fail("Failed with: " + e);
		}
	}

	@Test
	public void testMove() {
		try {
			byte[] stream;
			Piece p = Piece.createNew(Color.WHITE, PieceType.QUEEN);
			stream = writeObject(new Move(p, Coordinate.axial(0, 0), Coordinate.axial(1, 0)));
			assertEquals(new Move(p, Coordinate.axial(0, 0), Coordinate.axial(1, 0)), readObject(stream));

			p = Piece.createNew(Color.BLACK, PieceType.GRASSHOPER);
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
		
		pos.accept(new Move(Piece.createNew(Color.WHITE, PieceType.ANT), null, Coordinate.axial(0, 0)));
//		pos.accept(new Move(Piece.createNew(Color.BLACK, PieceType.BUG), null, Coordinate.axial(1, 0)));
//		pos.accept(new Move(pos.getPieceAt(Coordinate.axial(0, 0)), Coordinate.axial(0, 0), Coordinate.axial(1, 1)));
//		pos.accept(new Move(Piece.createNew(Color.BLACK, PieceType.QUEEN), null, Coordinate.axial(4, -2)));

		stream = writeObject(pos);
		assertEquals(pos, readObject(stream));
}

}
