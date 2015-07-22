package test.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.positions.Position;
import hive.positions.PositionUtils;
import test.common.TestUtils;

public class TestBug {

	@Test
	public void test() {
		Position pos = TestUtils.position1();
		
		{
			Coordinate[] expected = {Coordinate.axial(-1, -1), Coordinate.axial(0, -1),
					Coordinate.axial(0, 0), Coordinate.axial(-1, 1), Coordinate.axial(-2, 1)};
			assertEquals(TestUtils.toSet(expected), 
					PositionUtils.getBugMoves(pos, Coordinate.axial(-1, 0)));
		}

		{
			Coordinate[] expected = {Coordinate.axial(0, -2), Coordinate.axial(2, -3),
					Coordinate.axial(0, -1), Coordinate.axial(2, -2), Coordinate.axial(1, -1)};
			assertEquals(TestUtils.toSet(expected), 
					PositionUtils.getBugMoves(pos, Coordinate.axial(1, -2)));
		}
	}

}
