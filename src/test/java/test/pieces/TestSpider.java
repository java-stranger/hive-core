package test.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import hive.engine.Coordinate;
import hive.positions.Position;
import hive.positions.PositionUtils;
import test.common.TestUtils;

public class TestSpider {

	@Test
	public void test() {
		Position pos = TestUtils.position1();
		
		Coordinate[] expected = {Coordinate.axial(0, -2), Coordinate.axial(2, 0)};
		assertEquals(TestUtils.toSet(expected), PositionUtils.getSpiderMoves(pos, Coordinate.axial(2, -2)));
	}

}
