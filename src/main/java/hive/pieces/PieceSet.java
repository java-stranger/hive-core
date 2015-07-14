package hive.pieces;

import java.util.HashMap;
import java.util.Map.Entry;

import hive.engine.Player;

public class PieceSet {
	final static HashMap<PieceType, Integer> set;
	
	static { 
		set = new HashMap<>();
		set.put(PieceType.QUEEN, 1);
		set.put(PieceType.SPIDER, 2);
		set.put(PieceType.GRASSHOPER, 2);
		set.put(PieceType.ANT, 3);
		set.put(PieceType.BUG, 1);
	};
	
	public static void create(Player.Color color, Player p) {
		for(Entry<PieceType, Integer> entry: set.entrySet()) {
			for(int i = 0; i < entry.getValue(); ++i) {
				p.newPiece(Piece.createNew(color, entry.getKey()));
			}
		}
	}
	
}
