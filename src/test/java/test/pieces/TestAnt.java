package test.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.positions.Position;
import hive.positions.PositionUtils;
import test.common.TestUtils;

public class TestAnt {

	@Test
	public void test() {
		Position pos = TestUtils.position1();
		
		Coordinate[] expected_w = {
				Coordinate.axial(-1, 0), Coordinate.axial(-1, -1),
				Coordinate.axial(0, -2), Coordinate.axial(1, -2),
				Coordinate.axial(2, -3), Coordinate.axial(3, -3),
				Coordinate.axial(3, -2), Coordinate.axial(3, -1),
				Coordinate.axial(2, 0), Coordinate.axial(1, 1),
				Coordinate.axial(0, 1)
				};
		assertEquals(TestUtils.toSet(expected_w), PositionUtils.getAntMoves(pos, Coordinate.axial(-1, 1)));
	}

}
