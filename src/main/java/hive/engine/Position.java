package hive.engine;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import hive.pieces.Piece;
import hive.view.PlayingField;

public class Position implements Serializable {
	
	private static final long serialVersionUID = 6740990797043611124L;
	
	Player.Color next_turn = Player.Color.WHITE;
	private PlayingField view = null;

	private final HashMap<Piece, Coordinate> pieces = new HashMap<>();
	private final HashMap<Coordinate, Piece> coordinates = new HashMap<>();
	
	private static class SerializationProxy implements Serializable {
		private static final long serialVersionUID = 8277340064477621793L;
		
		final HashMap<Piece, Coordinate> pieces;
		Player.Color next_turn;
		SerializationProxy(Position pos) {
			this.pieces = pos.pieces;
			next_turn = pos.next_turn;
		}

		public Object readResolve() {
			Position pos = new Position();
			this.pieces.forEach((Piece p, Coordinate c) -> pos.insert(p, c));
			pos.next_turn = next_turn;
			return pos;
		}
	}
	
	public Object readResolve() throws InvalidObjectException {
		throw new InvalidObjectException("Requires proxy!");
	}
	
	private Object writeReplace() {
		return new SerializationProxy(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position)) {
			return false;
		}
		Position pos = (Position) obj;
		return this.next_turn == pos.next_turn 
				&& this.pieces.equals(pos.pieces)
				&& this.coordinates.equals(pos.coordinates);
	}
	
	@Override
	public String toString() {
		String res = "";
		if(pieces.size() < 4) {
			for(Entry<Piece, Coordinate> entry : pieces.entrySet()) {
				res += entry.getKey() + "@" + entry.getValue() + "; ";
			} 
		} else {
			res = "Has " + pieces.size() + " pieces; ";
		}
		
		return res + next_turn + "'s turn";
	}
	
	public void subscribeView(PlayingField view) {
		this.view = view;
	}
	
	public void reset() {
		pieces.clear();
		coordinates.clear();
		if(view != null) view.reset();
		next_turn = Player.Color.WHITE;
	}

	public void move(Piece p, Coordinate from, Coordinate to) {
		assert pieces.get(p) == from && coordinates.get(from) == p && isFree(to);
		pieces.put(p, to);
		coordinates.put(to, p);
		coordinates.remove(from);
		if(view != null) view.movePiece(p, from, to);
	}

	public void insert(Piece p, Coordinate at) {
		assert !pieces.containsKey(p) && isFree(at);
		pieces.put(p, at);
		coordinates.put(at, p);
		if(view != null) view.playPiece(p, at);
	}
	
	public void remove(Piece p) {
		assert pieces.containsKey(p);
		coordinates.remove(pieces.get(p), p);
		pieces.remove(p);
		if(view != null) view.removePiece(p);
	}
	
	public Coordinate getCoordinate(Piece p) {
		return pieces.get(p);
	}

	public Piece getPieceAt(Coordinate pos) {
		return coordinates.get(pos);
	}

	public boolean isFree(Coordinate pos) {
		return !coordinates.containsKey(pos);
	}

	static public Coordinate[] getNeighbors(Coordinate pos) {
		final Coordinate[] neighbors = 
			{ Coordinate.axial(pos.x()+0,pos.y()+1), Coordinate.axial(pos.x()+1,pos.y()+0),
			  Coordinate.axial(pos.x()+1,pos.y()-1), Coordinate.axial(pos.x()+0,pos.y()-1),
			  Coordinate.axial(pos.x()-1,pos.y()+0), Coordinate.axial(pos.x()-1,pos.y()+1) };
		return neighbors;
	}

	public HashSet<Coordinate> getAccesibleCells(Coordinate pos, int distance) {
		Piece p = coordinates.remove(pos); // temporarily remove piece from the field
		HashSet<Coordinate> visited = new HashSet<>();
		HashSet<Coordinate> to_visit;
		HashSet<Coordinate> to_visit_next = new HashSet<>();
		to_visit_next.add(pos);
		
		while(!to_visit_next.isEmpty()) {
			if(distance == 0) {
				break;
//				for(Coordinate v : to_visit_next)
//					visited.add(v);
//				break;
			}
			to_visit = to_visit_next;
			to_visit_next = new HashSet<>();
			for(Coordinate v : to_visit) {
				visited.add(v);
				for(Coordinate c : getAccesibleNeighboringCells(v)) {
					if(!visited.contains(c) && !to_visit.contains(c) && isAttached(c))
						to_visit_next.add(c);
				}
			}
			distance--;
		}
		
//		visited.remove(pos);
		coordinates.put(pos, p); // restore the piece
		return to_visit_next;
	}

	public HashSet<Coordinate> getAccesibleCellsRamping(Coordinate pos) {
		Piece piece = coordinates.remove(pos); // temporarily remove piece from the field
		HashSet<Coordinate> visited = new HashSet<>();
		HashSet<Coordinate> to_visit = new HashSet<>();
		to_visit.add(pos);
		
		while(!to_visit.isEmpty()) {
			Coordinate c = to_visit.iterator().next();
			visited.add(c);
			to_visit.remove(c);
			for(Coordinate n : getAccesibleNeighboringCells(c)) {
				if(!visited.contains(n) && isAttached(c))
					to_visit.add(n);
			}
		}
		
		coordinates.put(pos, piece); // restore the piece
		visited.remove(pos);
		return visited;
	}

	public boolean isAttached(Coordinate pos) {
		for(Coordinate n: getNeighbors(pos)) {
			if(!isFree(n))
				return true;
		}
		return false;
	}

	public HashSet<Coordinate> getAccesibleNeighboringCells(Coordinate pos) {
		HashSet<Coordinate> res = new HashSet<>();
		Coordinate[] neighbors = getNeighbors(pos); 
		boolean[] places = new boolean[neighbors.length];
		int i = 0;
		for(Coordinate n : neighbors) {
			places[i++] = isFree(n);
		}
		
		for(i = 0; i < places.length; ++i) {
			// we can move to a cell iff it's free, and at least one of its adjacent cells is free, too
			if(places[i] 
				&& (places[(i + places.length - 1) % places.length] != places[(i + 1) % places.length]))
					res.add(neighbors[i]);
		}
		return res;
	}
	
	public HashSet<Coordinate> getAccesibleForBugNeighboringCells(Coordinate pos) {
		Piece p = coordinates.remove(pos); // temporarily remove piece from the field
		HashSet<Coordinate> res = new HashSet<>();
		for(Coordinate n: getNeighbors(pos)) {
			if(isAttached(n))
				res.add(n);
		}
		coordinates.put(pos, p); // restore the piece
		return res;
	}
	
	public boolean stillConnectedIfRemoved(Piece without) {
		assert pieces.containsKey(without);

		if(pieces.size() == 1) return false; // the only piece, cannot move!
		
		Coordinate cell_to_ignore = pieces.get(without);
		
		HashSet<Coordinate> visited = new HashSet<Coordinate>();
		HashSet<Coordinate> to_visit = new HashSet<Coordinate>();

		// Start from any neighbor of the ignored cell 
		for(Coordinate n : getNeighbors(cell_to_ignore)) {
			if(!isFree(n)) {
				to_visit.add(n);
				break;
			}
		}
		assert !to_visit.isEmpty();
		
		// Now, perform BFS 
		while(!to_visit.isEmpty()) {
			Coordinate p = to_visit.iterator().next();
			visited.add(p);
			to_visit.remove(p);
			for(Coordinate n: getNeighbors(p)) {
				if(!isFree(n) && !visited.contains(n) && n != cell_to_ignore)
					to_visit.add(n);
			}
//			System.out.println("Visiting " + p);
		}
		return visited.size() == pieces.size() - 1; // -1 for the ignored cell
	}
	
	public Coordinate position(Piece p) {
		return pieces.get(p);
	}
	
	public boolean canMove(Piece p) {
		return stillConnectedIfRemoved(p);
	}

	public HashSet<Coordinate> getPossibleInsertionCells(Player.Color color) {
		HashSet<Coordinate> adjacentFree = new HashSet<>();
		if(pieces.isEmpty()) {
			adjacentFree.add(Coordinate.cube(0, 0, 0));
			return adjacentFree;
		} else if (pieces.size() == 1) {
			for(Coordinate n: getNeighbors(pieces.values().iterator().next())) {
				adjacentFree.add(n);
			}
			return adjacentFree;
		}
		
		// First, get all adjacent free cells
		for(Coordinate c: pieces.values()) {
			for(Coordinate n: getNeighbors(c)) {
				if(isFree(n)) {
					adjacentFree.add(n);
				}
			}
		}
		assert adjacentFree.isEmpty() == false;
		
		// Now, remove those that have adjacent of adversary color
		Iterator<Coordinate> it = adjacentFree.iterator();
		while(it.hasNext()){
			Coordinate c = it.next();
			for(Coordinate n: getNeighbors(c)) {
				Piece p = getPieceAt(n);
				if(p != null && p.color() != color) {
					it.remove();
					break;
				}
			}
		}
		assert !adjacentFree.isEmpty();
		return adjacentFree;
	}
	
	public Position accept(Move m) {
		assert next_turn == m.piece.color();
		
		if(m.from == null) {
			insert(m.piece, m.to);
			System.out.println(m.piece + " is inserted at " + m.to);
		} else {
			move(m.piece, m.from, m.to);
			System.out.println(m.piece + " is moved from " + m.from + " to " + m.to);
		}
		next_turn = next_turn.next();
		return this;
	}
	
	public Position undo(Move m) {
		assert next_turn != m.piece.color(); // can only undo the last move
		
		if(m.from == null) {
			remove(m.piece);
			System.out.println(m.piece + " is removed at " + m.to);
		} else {
			move(m.piece, m.to, m.from);
			System.out.println(m.piece + " is moved back from " + m.to + " to " + m.from);
		}
		next_turn = next_turn.next();
		return this;
	}

}
