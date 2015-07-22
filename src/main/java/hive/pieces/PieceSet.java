package hive.pieces;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import hive.player.IPlayer;

public class PieceSet {
	final static HashMap<PieceType, Integer> template = new HashMap<>();

	static { 
		template.put(PieceType.QUEEN, 1);
		template.put(PieceType.SPIDER, 2);
		template.put(PieceType.GRASSHOPER, 2);
		template.put(PieceType.ANT, 3);
		template.put(PieceType.BUG, 2);
	};
	
	protected class MutableInt {
		  int value = 0;
		  public int increment () { return ++value; }
		  public int  get ()       { return value; }
		  public int decrement () { return --value; }
		}

	final HashMap<Piece, MutableInt> set = new HashMap<>();
	
	private PieceSet(IPlayer.Color color) {
		populate(color, (Piece p) -> set.computeIfAbsent(p, (Piece pp) -> new MutableInt()).increment());
	}

	public static PieceSet createNew(IPlayer.Color color) {
		return new PieceSet(color);
	}

	private static void populate(IPlayer.Color color, Consumer<? super Piece> consumer) {
		for(Entry<PieceType, Integer> entry: template.entrySet()) {
			for(int i = 0; i < entry.getValue(); ++i) {
				consumer.accept(Piece.createNew(color, entry.getKey()));
			}
		}
	}
	
	public final void enumerate(BiConsumer<? super Piece, Integer > consumer) {
		set.entrySet().forEach((Entry<Piece, MutableInt> entry) -> {
			if(entry.getValue().get() > 0)
				consumer.accept(entry.getKey(), entry.getValue().get());
		});;
	}
	
	public final boolean hasPiece(Piece piece) {
		return set.get(piece).get() > 0;
	}

	public final void takeOut(Piece piece) {
		assert set.get(piece).get() > 0;
		set.get(piece).decrement();
	}

	public final void putBack(Piece piece) {
		set.get(piece).increment();
	}

}
