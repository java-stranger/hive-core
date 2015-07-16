package test.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.engine.Position;
import hive.engine.PositionUtils;
import test.common.TestUtils;

public class TestGrasshoper {

	@Test
	public void test() {
		Position pos = TestUtils.position1();
		
		{
			Coordinate[] expected = {Coordinate.axial(1, -1)};
			assertEquals(TestUtils.toSet(expected), 
					PositionUtils.getGrasshoperMoves(pos, Coordinate.axial(-1, -1)));
		}

		{
			Coordinate[] expected = {Coordinate.axial(-1, -1), Coordinate.axial(3, -1), 
					Coordinate.axial(3, -3), Coordinate.axial(-2, 2), Coordinate.axial(1, 1)};
			assertEquals(TestUtils.toSet(expected), 
					PositionUtils.getGrasshoperMoves(pos, Coordinate.axial(1, -1)));
		}

		{
			Coordinate[] expected = {Coordinate.axial(-1, 0), Coordinate.axial(2, -3)};
			assertEquals(TestUtils.toSet(expected), 
					PositionUtils.getGrasshoperMoves(pos, Coordinate.axial(2, 0)));
		}
}

}
