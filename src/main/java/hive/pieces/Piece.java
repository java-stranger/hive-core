package hive.pieces;

import java.io.Serializable;
import java.util.HashSet;

import hive.engine.Coordinate;
import hive.engine.Player;
import hive.engine.Position;

abstract public class Piece implements Serializable {
	
	private static final long serialVersionUID = 6433412988687976036L;

	Piece(Player.Color color, PieceType type) { 
		this.color = color; 
		this.type = type; 
	}
	
	final static HashSet<Coordinate> empty = new HashSet<>();
	
	final Player.Color color;
	final PieceType type;
	
	public PieceType getType() { return type; }
	public Player.Color color() { return color; } 
	
	public static Piece createNew(Player.Color color, PieceType type) {
		switch(type) {
		case QUEEN:
			return new Queen(color);
		case SPIDER:
			return new Spider(color);
		case ANT:
			return new Ant(color);
		case BUG:
			return new Bug(color);
		case GRASSHOPER:
			return new Grasshoper(color);
		default:
			System.out.println("Type [" + type + "] not implemented!");
			break;
		}
		return null;
	}
	
	public String toString() {
		return type + "[" + (color == Player.Color.WHITE ? "w" : "b") + "]";
	}
	
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof Piece)) {
			return false;
		}
		Piece p = (Piece) other;
		return this.type == p.type && this.color == p.color;
	}
	
	@Override 
	public int hashCode() {
		return 31 * color.hashCode() + type.hashCode();
	}
	
	abstract public HashSet<Coordinate> getPossibleMoves(Position position);
}
