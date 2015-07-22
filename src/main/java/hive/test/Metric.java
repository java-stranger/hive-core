package hive.test;

import java.util.ArrayList;

import hive.positions.Move;

public class Metric {
	public int numPiecesMoveableMe = 0;
	public int numPiecesMoveableAdv = 0;
	
	public ArrayList<Move> moves = new ArrayList<>();
	
	public boolean isBetter(Metric other) {
		return flatten() > other.flatten();
	}
	
	private double flatten() {
		return numPiecesMoveableMe - numPiecesMoveableAdv;
	}
	
	public Metric addFront(Move move) {
		moves.add(0, move);
		return this;
	}
	
	public String toString() {
		return "Score: " + flatten() + ", moves: " + moves;
	}
}
