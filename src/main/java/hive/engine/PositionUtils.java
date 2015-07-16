package hive.engine;

import java.util.HashSet;
import java.util.Iterator;

import hive.pieces.Piece;

public class PositionUtils {
		
	static public HashSet<Coordinate> getPossibleInsertionCells(Position pos, Player.Color color) {
		HashSet<Coordinate> adjacentFree = new HashSet<>();
		
		// First, get all adjacent free cells
		pos.iterateTopLevel((Coordinate c, Piece p) -> {
			for(Coordinate n: c.getNeighbors()) {
				if(pos.isFree(n)) {
					adjacentFree.add(n);
				}
			}
		});
		if(pos.numTopPieces() == 0) {
			// First move, always use (0,0) as insertion point
			adjacentFree.add(Coordinate.axial(0, 0));
			return adjacentFree;
		} else if (pos.numTopPieces() == 1) {
			// Second move, the only case when allowed to insert next to the adversary
			return adjacentFree;
		}
		
		// Now, remove those that have adjacent to them tiles of adversary color
		Iterator<Coordinate> it = adjacentFree.iterator();
		while(it.hasNext()){
			Coordinate c = it.next();
			for(Coordinate n: c.getNeighbors()) {
				Piece p = pos.getTopPieceAt(n);
				if(p != null && p.color() != color) {
					it.remove();
					break;
				}
			}
		}
		assert !adjacentFree.isEmpty();
		return adjacentFree;
	}
	
	static public HashSet<Coordinate> getQueenMoves(Position pos, Coordinate start) {
		return getBugOrQueenMoves(pos, start, false);
	}

	static public HashSet<Coordinate> getBugMoves(Position pos, Coordinate start) {
		return getBugOrQueenMoves(pos, start, true);
	}

	static private HashSet<Coordinate> getBugOrQueenMoves(Position pos, Coordinate start, boolean allowed_to_step_over) {
		HashSet<Coordinate> res = new HashSet<>();
		Coordinate[] neighbors = start.getNeighbors(); 
		boolean[] places = new boolean[neighbors.length];
		int i = 0;
		for(Coordinate n : neighbors) {
			places[i++] = pos.isFree(n);
		}
		
		for(i = 0; i < places.length; ++i) {
			if(allowed_to_step_over) {
				// we are not allowed to lose the connection with neighbors, all other moves are ok 
				if(!places[i] || !places[(i + places.length - 1) % places.length] || !places[(i + 1) % places.length])
					res.add(neighbors[i]);
			} else {
				// we can move to a cell iff it's free, and at least one of its adjacent cells is free, too
				if(places[i] 
					&& (places[(i + places.length - 1) % places.length] != places[(i + 1) % places.length]))
						res.add(neighbors[i]);
			}
		}
		return res;
	}
	
	static public HashSet<Coordinate> getSpiderMoves(Position position, Coordinate start) {
		return getSpiderMoves(position, start, 3);
	}

	static private HashSet<Coordinate> getSpiderMoves(Position position, Coordinate start, int distance) {
		Piece p = position.virtuallyRemove(start);
		HashSet<Coordinate> visited = new HashSet<>();
		HashSet<Coordinate> to_visit;
		HashSet<Coordinate> to_visit_next = new HashSet<>();
		to_visit_next.add(start);
		
		while(!to_visit_next.isEmpty()) {
			if(distance == 0) {
				break;
			}
			to_visit = to_visit_next;
			to_visit_next = new HashSet<>();
			for(Coordinate v : to_visit) {
				visited.add(v);
				for(Coordinate c : getQueenMoves(position, v)) {
					if(!visited.contains(c) && !to_visit.contains(c))
						to_visit_next.add(c);
				}
			}
			distance--;
		}
		
		position.virtuallyPlaceBack(p, start);
		return to_visit_next;
	}

	static public HashSet<Coordinate> getAntMoves(Position position, Coordinate start) {
		Piece p = position.virtuallyRemove(start);
		HashSet<Coordinate> visited = new HashSet<>();
		HashSet<Coordinate> to_visit = new HashSet<>();
		to_visit.add(start);
		
		while(!to_visit.isEmpty()) {
			Coordinate c = to_visit.iterator().next();
			visited.add(c);
			to_visit.remove(c);
			for(Coordinate n : getQueenMoves(position, c)) {
				if(!visited.contains(n))
					to_visit.add(n);
			}
		}
		
		position.virtuallyPlaceBack(p, start);
		visited.remove(start);
		return visited;
	}

	static public HashSet<Coordinate> getGrasshoperMoves(Position position, Coordinate start) {
		HashSet<Coordinate> res = new HashSet<>();
		// can move in three directions, jumping over continuous tiles:
		final int[][] coords = {{1, -1, 0}, {-1, 1, 0}, {0, 1, -1}, {0, -1, 1}, {1, 0, -1}, {-1, 0, 1}};
		
		for(int[] dir : coords) {
			Coordinate to;
			int len = 1;
			for(len = 1; ;++len) {
				to = Coordinate.cube(start.x() + dir[0] * len, start.y() + dir[1] * len, start.z() + dir[2] * len);
				if(position.isFree(to))
					break;
			}
			if(len != 1)
				res.add(to);
		}
		return res;
	}
	
	public static boolean cellConnected(Position position, Coordinate c) {
		for(Coordinate n: c.getNeighbors()) {
			if(!position.isFree(n))
				return true;
		}
		return false;
	}
	
	public static boolean connected(Position position) {
		// check if every piece has at least one neighbor
		if(position.numTopPieces() == 1)
			return true;
		
		HashSet<Coordinate> connected = new HashSet<>();
		HashSet<Coordinate> to_check = new HashSet<>();
		
		// initialize to_check with the first (any) element
		position.iterateTopLevelWhile((Coordinate c, Piece p) -> { to_check.add(c); return false; } );
		
		while(!to_check.isEmpty()) {
			Coordinate c = to_check.iterator().next();
			to_check.remove(c);
			connected.add(c);
			for(Coordinate n: c.getNeighbors()) {
				if(!position.isFree(n) && !connected.contains(n))
					to_check.add(n);
			}
		}
		
		return position.numTopPieces() == connected.size();
	}



}
