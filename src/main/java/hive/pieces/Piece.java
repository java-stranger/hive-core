package hive.pieces;

import java.io.Serializable;
import java.util.HashSet;

import hive.engine.Coordinate;
import hive.player.IPlayer;
import hive.positions.IPosition;
import hive.positions.PositionUtils;

abstract public class Piece implements Serializable {
	
	private static final long serialVersionUID = 6433412988687976036L;

	Piece(IPlayer.Color color, PieceType type) { 
		this.color = color; 
		this.type = type; 
	}
	
	final static HashSet<Coordinate> empty = new HashSet<>();
	
	final IPlayer.Color color;
	final PieceType type;
	
	public PieceType getType() { return type; }
	public IPlayer.Color color() { return color; } 
	
	public static Piece createNew(IPlayer.Color color, PieceType type) {
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
		return type + "[" + (color == IPlayer.Color.WHITE ? "w" : "b") + "]";
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
		return 17 + 31 * color.hashCode() + type.hashCode();
	}
	
	abstract public HashSet<Coordinate> getMoves(IPosition position, Coordinate current);
	
	public HashSet<Coordinate> getPossibleMoves(IPosition position, Coordinate current) {
		if(PositionUtils.canMove(position, this, current)) {
			return getMoves(position, current);
		}
		else 
			return empty;
	}
	
	public boolean canSitOnTopOfOthers() {
		return false;
	}
}
