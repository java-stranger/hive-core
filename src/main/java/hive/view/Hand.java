package hive.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public class Hand {
	private final Coordinate offset;
	private final boolean inverse;
	
	private final ArrayList<Coordinate> availableCells = new ArrayList<>();
	private final HashMap<Piece, ArrayList<Coordinate>> allocated = new HashMap<>();

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
	
	public Coordinate allocate(Piece p) {
		assert !availableCells.isEmpty();
		Coordinate c = availableCells.remove(0); 
		allocated.computeIfAbsent(p, (Piece piece) -> new ArrayList<Coordinate>()).add(c);
//		System.out.println("Putting " + p + "@" + c + "(hand)" + this);
		return c;
	}
	
	public void deallocate(Piece p, Coordinate c) {
		assert !availableCells.contains(c) && allocated.containsKey(p) && allocated.get(p).contains(c);
		allocated.get(p).remove(c);
		availableCells.add(0, c);
//		System.out.println("Removing " + p + " from " + c + "(hand)" + this);
	}

	public Coordinate deallocate(Piece p) {
		assert allocated.containsKey(p);
		Coordinate c = allocated.get(p).remove(0);
		availableCells.add(0, c);
//		System.out.println("Removing " + p + " from " + c + "(hand)" + this);
		return c;
	}
	
	public Piece getPieceAt(Coordinate c) {
		for(Entry<Piece, ArrayList<Coordinate>> entry : allocated.entrySet()) {
			if(entry.getValue().contains(c)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
