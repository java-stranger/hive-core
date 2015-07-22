package hive.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

public class Hand {
	private final Coordinate offset;
	private final boolean inverse;
	
	private ArrayList<Coordinate> availableCells = new ArrayList<>();
	private LinkedHashMap<Coordinate, Piece> allocated = new LinkedHashMap<>();

	int gridNo = 1;
	
	final IPosition position;
	
	Hand(IPosition position, Coordinate offset, boolean inversed) {
		this.offset = offset;
		this.inverse = inversed;
		this.position = position;
		
		initGrid(gridNo, inversed, offset, null);
	}
	
	private void initGrid(int gridNo, boolean inversed, Coordinate offset, HashSet<Coordinate> avoid) {
		availableCells.clear();
		for(Coordinate c : grids[gridNo]) {
			if(inversed)
				availableCells.add(c.sum(offset).inverse());
			else
				availableCells.add(c.sum(offset));
		}		
		if(avoid != null) {
			availableCells.removeAll(avoid);
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
				Coordinate.axial(0, 5), Coordinate.axial(1, 5), 
				Coordinate.axial(0, 6), Coordinate.axial(1, 6), 
				Coordinate.axial(0, 7), Coordinate.axial(1, 7), 
				Coordinate.axial(0, 8), Coordinate.axial(1, 8), 
				Coordinate.axial(0, 9), Coordinate.axial(1, 9), 
			}
	};
	
	public Coordinate placeInHand(Piece p) {
		assert !availableCells.isEmpty();
		Coordinate c = availableCells.remove(0);
		Piece prev = allocated.put(c, p);
		assert prev == null;
//		System.out.println("Putting " + p + "@" + c + "(hand)" + this);
		return c;
	}
	
	public void removeFromHand(Piece p, Coordinate c) {
		assert !availableCells.contains(c) && allocated.containsKey(c) && allocated.get(c).equals(p);
		allocated.remove(c);
		availableCells.add(0, c);
//		System.out.println("Removing " + p + " from " + c + "(hand)" + this);
	}

	public Coordinate removeFromHandAnyOfType(Piece p) {
		for(Entry<Coordinate, Piece> entry : allocated.entrySet()) {
			if(entry.getValue().equals(p)) {
				removeFromHand(p, entry.getKey());
				return entry.getKey();
			}
		}
		assert false;
		return null;
	}
	
	public Piece getPieceAt(Coordinate c) {
		return allocated.get(c);
	}
	
	public void rearrangeToAvoidClash(int gridNo, Renderer renderer) {
		LinkedHashMap<Coordinate, Piece> oldAllocated = allocated;
		allocated = new LinkedHashMap<>();
		initGrid(gridNo, inverse, offset, PositionUtils.getExternalBorder(position));
		oldAllocated.entrySet().forEach((Entry<Coordinate, Piece> entry) -> {
			Coordinate to = placeInHand(entry.getValue()); 
			if(renderer != null) renderer.movePiece(entry.getValue(), entry.getKey(), to);
		});
	}
}
