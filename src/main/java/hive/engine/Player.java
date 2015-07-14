package main.java.hive.engine;

import java.util.ArrayList;

import main.java.hive.pieces.Piece;
import main.java.hive.pieces.PieceSet;
import main.java.hive.pieces.PieceType;
import main.java.hive.view.Hand;

public abstract class Player {
	protected ArrayList<Piece> remaining = new ArrayList<>();
	
	public enum Color {
		WHITE { @Override Color next() { return BLACK;} },
		BLACK { @Override Color next() { return WHITE;} };
		
		abstract Color next();
	}
	
	private final Color color;
	
	Hand view = null;
	
	protected Player(Color color) {
		this.color = color;
		reset();
	}
	
	public final void reset() {
		remaining.clear();
		PieceSet.create(color, this);
	}
	
	public final ArrayList<Piece> getPieces() {
		return remaining;
	}

	public void newPiece(Piece piece) {
		remaining.add(piece);
	}
	
	public Player.Color color() { return color; } 

	public Piece getPiece(PieceType type) {
		for(Piece p : remaining) {
			if(type == null || p.getType() == type) {
				remaining.remove(p);
				return p;
			}
		}
		return null;
	}

	public String toString() {
		if(color == Color.WHITE)
			return "White";
		else
			return "Black";
	}
	
	abstract public Move nextMove(Position board);
}
