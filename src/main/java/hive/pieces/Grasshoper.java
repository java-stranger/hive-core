package hive.pieces;

import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;

public class Grasshoper extends Piece {
	Grasshoper(Player.Color color) {
		super(color, PieceType.GRASSHOPER);
	}
	
	@Override
	public HashSet<Coordinate> getPossibleMoves(Position board) {
		Coordinate pos = board.position(this);
		assert pos != null;
		if(board.canMove(this)) {
			HashSet<Coordinate> res = new HashSet<>();
			// can move in three directions, jumping over continuous tiles:
			final int[][] coords = {{1, -1, 0}, {-1, 1, 0}, {0, 1, -1}, {0, -1, 1}, {1, 0, -1}, {-1, 0, 1}};
			
			for(int[] dir : coords) {
				Coordinate to;
				int len = 1;
				for(len = 1; ;++len) {
					to = Coordinate.cube(pos.x() + dir[0] * len, pos.y() + dir[1] * len, pos.z() + dir[2] * len);
					if(board.isFree(to))
						break;
				}
				if(len != 1)
					res.add(to);
			}
			return res;
		}
		else 
			return empty;
	}

}
