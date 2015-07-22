package hive.positions;

import java.util.HashSet;
import java.util.Iterator;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.pieces.PieceType;
import hive.player.IPlayer;

public class PositionUtils {
	
	static public HashSet<Coordinate> getExternalBorder(IPosition pos) {
		HashSet<Coordinate> first_round = new HashSet<>();
		
		// First, get all adjacent free cells
		pos.forEachVisible((Coordinate c, Piece p) -> {
			for(Coordinate n: c.getNeighbors()) {
				if(pos.isFree(n)) {
					first_round.add(n);
				}
			}
		});
		
		HashSet<Coordinate> border = new HashSet<>();
		for(Coordinate current : first_round) {
			for(Coordinate n: current.getNeighbors()) {
				if(pos.isFree(n) && !first_round.contains(n)) {
					border.add(n);
				}
			}
		}
		
		border.addAll(first_round);
		return border;
	}
		
	static public HashSet<Coordinate> getPossibleInsertionCells(IPosition pos, IPlayer.Color color) {
		HashSet<Coordinate> adjacentFree = new HashSet<>();
		
		// First, get all adjacent free cells
		pos.forEachVisible((Coordinate c, Piece p) -> {
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

	static public HashSet<Coordinate> getQueenMoves(IPosition pos, Coordinate start) {
		return getBugOrQueenMoves(pos, start, false, null);
	}

	static public HashSet<Coordinate> getBugMoves(IPosition pos, Coordinate start) {
		return getBugOrQueenMoves(pos, start, true, null);
	}

	static private HashSet<Coordinate> getQueenMoves(IPosition pos, Coordinate start, Coordinate ignore) {
		return getBugOrQueenMoves(pos, start, false, ignore);
	}

	static private HashSet<Coordinate> getBugOrQueenMoves(IPosition pos, Coordinate start, 
														 boolean allowed_to_step_over, Coordinate ignore) {
		HashSet<Coordinate> res = new HashSet<>();
		Coordinate[] neighbors = start.getNeighbors(); 
		boolean[] places = new boolean[neighbors.length];
		int i = 0;
		for(Coordinate n : neighbors) {
			places[i++] = (!n.equals(ignore) ? pos.isFree(n) : (pos.getPieceAt(n, 1) == null));
		}
		
		for(i = 0; i < places.length; ++i) {
			if(allowed_to_step_over) {
				// we are not allowed to lose the connection with neighbors, all other moves are ok 
				if(pos.getPieceAt(start, 1) != null || !places[i] || !places[(i + places.length - 1) % places.length] || !places[(i + 1) % places.length])
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
	
	static public HashSet<Coordinate> getSpiderMoves(IPosition position, Coordinate start) {
		return getSpiderMoves(position, start, 3);
	}

	static private HashSet<Coordinate> getSpiderMoves(IPosition position, Coordinate start, int distance) {
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
				for(Coordinate c : getQueenMoves(position, v, start)) {
					if(!visited.contains(c) && !to_visit.contains(c))
						to_visit_next.add(c);
				}
			}
			distance--;
		}
		
		return to_visit_next;
	}

	static public HashSet<Coordinate> getAntMoves(IPosition position, Coordinate start) {
		HashSet<Coordinate> visited = new HashSet<>();
		HashSet<Coordinate> to_visit = new HashSet<>();
		to_visit.add(start);
		
		while(!to_visit.isEmpty()) {
			Coordinate c = to_visit.iterator().next();
			visited.add(c);
			to_visit.remove(c);
			for(Coordinate n : getQueenMoves(position, c, start)) {
				if(!visited.contains(n))
					to_visit.add(n);
			}
		}
		
		visited.remove(start);
		return visited;
	}

	static public HashSet<Coordinate> getGrasshoperMoves(IPosition position, Coordinate start) {
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
	
	public static boolean cellConnected(IPosition position, Coordinate c) {
		for(Coordinate n: c.getNeighbors()) {
			if(!position.isFree(n))
				return true;
		}
		return false;
	}
	
	private static void expand(IPosition position, Coordinate start, HashSet<Coordinate> set) {
		if(!set.add(start))
			return;
		for(Coordinate n: start.getNeighbors()) {
			if(!position.isFree(n)) {
				expand(position, n, set);
			}
		}		
	}
	
	public static boolean connected(IPosition position) {
		HashSet<Coordinate> connected = new HashSet<>();
		Coordinate start = position.getAnyPoint();
		if(start == null)
			return true;
		
		expand(position, start, connected);
		
		return position.numTopPieces() == connected.size();
		
//		
//		HashSet<Coordinate> to_check = new HashSet<>();
//		
//		// initialize to_check with the first (any) element
//		position.forEachVisibleWhileTrue((Coordinate c, Piece p) -> { 
//			if(c != ignore) {
//				to_check.add(c); 
//				return false; 
//			}
//			return true; 
//		});
//
//		if(to_check.isEmpty()) // the only visible piece is that one we must ignore, so...
//			return true;
//		
//		while(!to_check.isEmpty()) {
//			Coordinate c = to_check.iterator().next();
//			to_check.remove(c);
//			connected.add(c);
//			for(Coordinate n: c.getNeighbors()) {
//				if(!position.isFree(n) && !connected.contains(n) && n != ignore)
//					to_check.add(n);
//			}
//		}
//		
//		return position.numTopPieces() - 1 == connected.size();
	}
	
	static class LegInfo {
		public final int leg1;
		public final int leg2;
		public final int leg3;
		LegInfo(int l1, int l2, int l3) {
			leg1 = l1; leg2 = l2; leg3 = l3;
		}
		public String toString() {
			return leg1 + " " + leg2 + " " + leg3;
		}
	}
	
	static boolean[] isBlocked = new boolean[64];
	static boolean[] canFreelyMove = new boolean[64];
	static LegInfo[] legInfo = new LegInfo[64];
	
	private static boolean free(int mask, int pos) {
		return (mask & (1 << pos)) == 0;
	}
	
	static {
		for(int mask = 0; mask < isBlocked.length; ++mask) {
			int emask = (mask << 1) | ((mask & 1) << 7) | ((mask & 32) >> 5);
			System.out.println(Integer.toBinaryString(mask) + " -> " + Integer.toBinaryString(emask));
			int num_possible = 0, num_changes = 0;
			int[] legs = {-1, -1, -1};
			int leg_no = 0;
			for(int z = 1; z < 7; ++z) {
				if(free(emask, z) && (free(emask, z - 1) != free(emask, z + 1))) {
					num_possible++;
				}
				if(free(emask, z) != free(emask, z - 1)) {
					num_changes++;
					if(!free(emask, z)) {
						legs[leg_no++] = z - 1;
					}
				}
			}
			isBlocked[mask] = num_possible == 0;
			canFreelyMove[mask] = (num_possible != 0) && (num_changes == 2);
			legInfo[mask] = new LegInfo(legs[0], legs[1], legs[2]);
			System.out.println("Possible moves: " + num_possible);
			System.out.println("Num changes: " + num_changes);
			System.out.println("Legs: " + legInfo[mask]);
		}
	}
	
	public static HashSet<Coordinate> path = new HashSet<>();

	public static boolean canMove(IPosition position, Piece piece, Coordinate item) {
		assert piece.equals(position.getTopPieceAt(item));

		path.clear();

//		System.out.println(piece + "@" + item + " canmove");

		if(position.getPieceAt(item, 1) != null)
			return true; // bug sitting on top of another piece can always move
		
		int i = 1, mask = 0;
		for(Coordinate n : item.getNeighbors()) {
			mask = mask | (!position.isFree(n) ? i : 0);
			i = i << 1;
		}

//		System.out.println(piece + "@" + item + ": mask " + Integer.toBinaryString(mask));

		if(canFreelyMove[mask]) {
//			System.out.println(piece + " can freely move");
			return true;
		} else if(isBlocked[mask] && (piece.getType() != PieceType.BUG || piece.getType() != PieceType.GRASSHOPER)) {
//			System.out.println(piece + " is blocked by others");
			return false;
		}

//		System.out.println("Checking if still connected when removed");

		// need to check the connection
		if(mask == 0b101010 || mask == 0b010101) {
			// special case, 3 "legs"
			path.add(item);
			if(!buildShortestRoute(position, path, item.getNeighbors()[legInfo[mask].leg1], 
					legInfo[mask].leg1, item.getNeighbors()[legInfo[mask].leg2]))
				return false; // cannot connect leg1 & leg2
			if( path.contains(item.getNeighbors()[legInfo[mask].leg3]))
				return true;
			path.clear();
			return buildShortestRoute(position, path, item.getNeighbors()[legInfo[mask].leg2],
							legInfo[mask].leg2, item.getNeighbors()[legInfo[mask].leg3]);
		} else {
			// Common 2 "legs"-case: need to find a path from one leg to the other
			path.add(item);
			return buildShortestRoute(position, path, item.getNeighbors()[legInfo[mask].leg1], 
					legInfo[mask].leg1, item.getNeighbors()[legInfo[mask].leg2]);
		}
	}
	
	
	static boolean buildShortestRoute(IPosition position, HashSet<Coordinate> path, Coordinate next, int next_pos, Coordinate starting_point) {
		if(next.equals(starting_point))
			return true;
		path.add(next);
		
		Coordinate[] neighbors = next.getNeighbors();
		assert position.isFree(neighbors[(next_pos + 4) % neighbors.length]);

		next_pos += 5;
		for(int i = next_pos; i < next_pos + neighbors.length; ++i ) {
			int imod = i % neighbors.length;
			Coordinate n = neighbors[imod];
			if(!path.contains(n) && !position.isFree(n)) {
				if(buildShortestRoute(position, path, n, imod, starting_point))
					return true;
			}
		}
		return false;
	}

}
