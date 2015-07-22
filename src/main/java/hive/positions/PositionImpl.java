package hive.positions;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import hive.engine.Coordinate;
import hive.pieces.Piece;

public class PositionImpl implements Serializable, IPositionImpl {

	private static final long serialVersionUID = 2036693900193049436L;
	
	protected final HashMap<Coordinate, ArrayList<Piece>> coordinates = new HashMap<>();
	
	private static class SerializationProxy implements Serializable {
		private static final long serialVersionUID = 8277340064477621793L;
		
		final ArrayList<Entry<Coordinate, Piece>> piecesCollection = new ArrayList<>();
		SerializationProxy(PositionImpl pos) {
			pos.coordinates.entrySet().forEach((Entry<Coordinate, ArrayList<Piece>> entry) -> {
				if(entry.getValue() != null) {
					entry.getValue().forEach((Piece p) 
							-> piecesCollection.add(new SimpleEntry<Coordinate, Piece>(entry.getKey(), p)));
				}
			});
		}

		public Object readResolve() {
			PositionImpl pos = new PositionImpl();
			this.piecesCollection.forEach((Entry<Coordinate, Piece> e) 
					-> pos.insert(e.getValue(), e.getKey()));
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
		if(!(obj instanceof PositionImpl)) {
			return false;
		}
		PositionImpl pos = (PositionImpl) obj;
		return this.coordinates.equals(pos.coordinates);
	}

	public String toString() {
		String res = "";
		if(coordinates.size() < 4) {
			for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet()) {
				res += entry.getValue() + "@" + entry.getKey() + "; ";
			} 
		} else {
			res = "Has " + coordinates.size() + " pieces";
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#forEach(java.util.function.BiConsumer)
	 */
	@Override
	public void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			for(Piece p : entry.getValue())
				consumer.accept(entry.getKey(), p);
	}
	
	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#forEachVisible(java.util.function.BiConsumer)
	 */
	@Override
	public void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			if(!entry.getValue().isEmpty())
				consumer.accept(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1));
	}
	
	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#forEachVisibleWhileTrue(java.util.function.BiFunction)
	 */
	@Override
	public boolean forEachVisibleWhileTrue(BiFunction<? super Coordinate, ? super Piece, Boolean> consumer) {
		for(Entry<Coordinate, ArrayList<Piece>> entry : coordinates.entrySet())
			if(!entry.getValue().isEmpty())
				if(!consumer.apply(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1)))
					return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#getNumVisiblePieces()
	 */
	@Override
	public int getNumVisiblePieces() {
		return coordinates.size();
	}
	
	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#reset()
	 */
	@Override
	public void reset() {
		coordinates.clear();
	}
	
	public Piece move(Coordinate from, Coordinate to) {
		Piece p = remove(from); 
		insert(p, to);
		return p; // for control
	}

	public void insert(Piece p, Coordinate at) {
		coordinates.computeIfAbsent(at, (Coordinate c) -> new ArrayList<Piece>()).add(p);
	}
	
	public Piece remove(Coordinate at) {
		Piece p = coordinates.get(at).remove(coordinates.get(at).size() - 1);
		if (coordinates.get(at).isEmpty()) coordinates.remove(at);
		return p;
	}
	
	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#getPieceAt(hive.engine.Coordinate, int)
	 */
	@Override
	public Piece getPieceAt(Coordinate pos, int i) {
		if(coordinates.get(pos) == null || coordinates.get(pos).size() <= i)
			return null;
		return coordinates.get(pos).get(coordinates.get(pos).size() - i - 1);
	}

	/* (non-Javadoc)
	 * @see hive.positions.IPositionImpl#isFree(hive.engine.Coordinate)
	 */
	@Override
	public boolean isFree(Coordinate pos) {
		return !coordinates.containsKey(pos);
	}
	
	public Coordinate getAny() {
		return coordinates.isEmpty() ? null : coordinates.keySet().iterator().next();
	}



}
