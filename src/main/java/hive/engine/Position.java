package hive.engine;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import hive.pieces.Piece;
import hive.view.PlayingField;

/**
 * @author mac
 *
 */
public class Position implements Serializable {
	
	private static final long serialVersionUID = 6740990797043611124L;
	
	Player.Color next_turn = Player.Color.WHITE;
	private PlayingField view = null;

	private final HashMap<Coordinate, ArrayList<Piece>> coordinates = new HashMap<>();
	
	private static class SerializationProxy implements Serializable {
		private static final long serialVersionUID = 8277340064477621793L;
		
		final ArrayList<Entry<Coordinate, Piece>> piecesCollection = new ArrayList<>();
		Player.Color next_turn;
		SerializationProxy(Position pos) {
			next_turn = pos.next_turn;
			pos.coordinates.entrySet().forEach((Entry<Coordinate, ArrayList<Piece>> entry) -> {
				if(entry.getValue() != null) {
					entry.getValue().forEach((Piece p) 
							-> piecesCollection.add(new SimpleEntry<Coordinate, Piece>(entry.getKey(), p)));
				}
			});
		}

		public Object readResolve() {
			Position pos = new Position();
			this.piecesCollection.forEach((Entry<Coordinate, Piece> e) 
					-> pos.insert(e.getValue(), e.getKey()));
			pos.next_turn = next_turn;
			pos.checkInvariants();
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
				&& this.coordinates.equals(pos.coordinates);
	}
	
	@Override
	public String toString() {
		String res = "";
		if(coordinates.size() < 4) {
			for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet()) {
				res += entry.getValue() + "@" + entry.getKey() + "; ";
			} 
		} else {
			res = "Has " + coordinates.size() + " pieces; ";
		}
		
		return res + next_turn + "'s turn";
	}
	
	public void subscribeView(PlayingField view) {
		this.view = view;
	}
	
	public void iterateAll(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			for(Piece p : entry.getValue())
				consumer.accept(entry.getKey(), p);
	}
	
	public void iterateTopLevel(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			consumer.accept(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1));
	}
	
	public boolean iterateTopLevelWhile(BiFunction<? super Coordinate, ? super Piece, Boolean> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			if(!consumer.apply(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1)))
				return false;
		return true;
	}
	
	/**
	 * @return Number of visible (top-level) pieces. Might be different from the total number
	 * of pieces (if some of them are hidden by bugs)
	 */
	public int numTopPieces() {
		return coordinates.size();
	}
	
	public void reset() {
		if(view != null) {
			iterateAll((Coordinate c, Piece p) -> view.removePiece(p, c));
		}
		coordinates.clear();
		next_turn = Player.Color.WHITE;
	}

	private void move(Piece p, Coordinate from, Coordinate to) {
		if(coordinates.get(from) == null || !coordinates.get(from).contains(p)
				|| (!isFree(to) && !p.canSitOnTopOfOthers()))
			throw new IllegalStateException("Cannot move "+ p + " from "+ from + "to" + to + "!");
		
		coordinates.computeIfAbsent(to, (Coordinate c) -> new ArrayList<Piece>()).add(p);
		coordinates.get(from).remove(p);
		if (coordinates.get(from).isEmpty()) coordinates.remove(from);
		if(view != null) view.movePiece(p, from, to);
	}

	private void insert(Piece p, Coordinate at) {
		if(!isFree(at)) 
			throw new IllegalStateException("Cannot insert@"+ at + ": already taken!");
		
		coordinates.computeIfAbsent(at, (Coordinate c) -> new ArrayList<Piece>()).add(p);
		if(view != null) view.playPiece(p, at);
	}
	
	private void remove(Piece p, Coordinate at) {
		if(coordinates.get(at) == null || !coordinates.get(at).contains(p))
			throw new IllegalStateException("Cannot remove "+ p + "@"+ at + ": not found!");
		coordinates.get(at).remove(p);
		if (coordinates.get(at).isEmpty()) coordinates.remove(at);
		if(view != null) view.removePiece(p, at);
	}
	
	public Piece getTopPieceAt(Coordinate pos) {
		if(coordinates.get(pos) == null)
			return null;
		return coordinates.get(pos).get(coordinates.get(pos).size() - 1);
	}

	public boolean isFree(Coordinate pos) {
		return !coordinates.containsKey(pos);
	}
	
	private void checkInvariants() {
		if(!PositionUtils.connected(this))
			throw new IllegalStateException("Would result in disconnected position!");
	}
		
	public boolean canMove(Coordinate item) {
		Piece p = virtuallyRemove(item);
		boolean canMove = PositionUtils.connected(this);
		virtuallyPlaceBack(p, item);
		return canMove;
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
		
		checkInvariants();
		return this;
	}
	
	public Position undo(Move m) {
		assert next_turn != m.piece.color(); // can only undo the last move
		
		if(m.from == null) {
			remove(m.piece, m.to);
			System.out.println(m.piece + " is removed at " + m.to);
		} else {
			move(m.piece, m.to, m.from);
			System.out.println(m.piece + " is moved back from " + m.to + " to " + m.from);
		}
		next_turn = next_turn.next();

		checkInvariants();
		return this;
	}
	
	Piece virtuallyRemove(Coordinate at) {
		Piece p = coordinates.get(at).remove(coordinates.get(at).size() - 1); 
		if (coordinates.get(at).isEmpty()) coordinates.remove(at);
		return p;
	}

	void virtuallyPlaceBack(Piece p, Coordinate at) {
		coordinates.computeIfAbsent(at, (Coordinate c) -> new ArrayList<Piece>()).add(p);
	}

}
