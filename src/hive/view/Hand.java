package hive.view;

import java.util.ArrayList;

import hive.engine.Coordinate;

public class Hand {
	private final Coordinate offset;
	private final boolean inverse;
	
	private final ArrayList<Coordinate> availableCells = new ArrayList<>();

	Hand(Coordinate offset, boolean inversed) {
		this.offset = offset;
		this.inverse = inversed;
		
		initGrid(1);
	}
	
	private void initGrid(int gridNo) {
		availableCells.clear();
		for(Coordinate c : grids[gridNo]) {
			if(inverse)
				availableCells.add(c.sum(offset).inverse());
			else
				availableCells.add(c.sum(offset));
		}		
	}
	
	// Different layout for the hand, for fanciness
	private final Coordinate[][] grids = { 
			{
				Coordinate.axial(0, 0), Coordinate.axial(0, 1), 	
				Coordinate.axial(0, 2), Coordinate.axial(0, 3), 	
				Coordinate.axial(0, 4), Coordinate.axial(0, 5), 	
				Coordinate.axial(0, 6), Coordinate.axial(0, 7), 	
				Coordinate.axial(0, 8), Coordinate.axial(0, 9), 
			}, 
			{
				Coordinate.axial(2, -1), Coordinate.axial(1, 0), Coordinate.axial(0, 0), 	
				Coordinate.axial(0, 1), Coordinate.axial(1, 1), 	
				Coordinate.axial(1, 2), Coordinate.axial(0, 2), 	
				Coordinate.axial(0, 3), Coordinate.axial(0, 4), 	
				Coordinate.axial(1, 3), Coordinate.axial(1, 4), 
			}
	};
	
	public Coordinate getNextPos() {
		assert !availableCells.isEmpty();
		return availableCells.remove(0);
	}
	
	public void putBack(Coordinate c) {
		assert !availableCells.contains(c);
		availableCells.add(0, c);
	}
}
