package test.pieces;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.engine.Position;
import hive.engine.PositionUtils;
import test.common.TestUtils;

public class TestQueen {

	@Test
	public void test() {
		Position pos = TestUtils.position1();
		
		Coordinate[] expected_w = {Coordinate.axial(1, -1), Coordinate.axial(-1, 0)};
		assertEquals(TestUtils.toSet(expected_w), PositionUtils.getQueenMoves(pos, Coordinate.axial(0, -1)));
	}

}
