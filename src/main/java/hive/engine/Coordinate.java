package hive.engine;

import java.io.Serializable;
import java.util.HashMap;

public final class Coordinate implements Serializable {
	
	private static final long serialVersionUID = -1924619317037201884L;

	private Coordinate(int x, int y) { 
		this.x = x; 
		this.y = y; 
		}
	
	static HashMap<Integer, HashMap<Integer, Coordinate>> pool = new HashMap<>();
	
	private Object readResolve() {
		return getInstance(this.x, this.y);
	}
	
	private static Coordinate getInstance(int x, int y) {
		HashMap<Integer, Coordinate> row = pool.getOrDefault(x,	null);
		if(row == null) {
			row = new HashMap<Integer, Coordinate>();
			pool.put(x, row);
		}
		Coordinate cell = row.getOrDefault(y, null);
		if(cell == null) {
			cell = new Coordinate(x, y);
			row.put(y, cell);
		}
		return cell;
	}
	
	public static Coordinate cube(int x, int y, int z) {
		assert x + y + z == 0;
		return getInstance(x, y);
	}
	
	public static Coordinate axial(int x, int y) {
		return getInstance(x, y);
	}
	
	public Coordinate sum(Coordinate another) {
		return axial(this.x + another.x, this.y + another.y);
	}

	public Coordinate inverse() {
		return axial(-this.x, -this.y);
	}

	private final int x;
	private final int y;
	
	public int x() { return x; }
	public int y() { return y; }
	public int z() { return -x-y; }
	
	public String toString() {
		return "(" + x + "," + y +"," + z()+")";
	}
	
	public Coordinate[] getNeighbors() {
		final Coordinate[] neighbors = 
			{ Coordinate.axial(x+0,y+1), Coordinate.axial(x+1,y+0),
			  Coordinate.axial(x+1,y-1), Coordinate.axial(x+0,y-1),
			  Coordinate.axial(x-1,y+0), Coordinate.axial(x-1,y+1) };
		return neighbors;
	}

}
