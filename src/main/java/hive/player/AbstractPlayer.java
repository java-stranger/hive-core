package hive.player;

import java.util.function.BiConsumer;

import hive.pieces.Piece;
import hive.pieces.PieceSet;
import hive.positions.Move;

public abstract class AbstractPlayer implements IPlayer {
		
	protected final IPlayer.Color color;
	protected PieceSet remainingPieces;
	
	public AbstractPlayer(IPlayer.Color color) {
		this.color = color;
		remainingPieces = PieceSet.createNew(color);
	}
	
	@Override
	public void reset() {
		remainingPieces = PieceSet.createNew(color);
	}
	
	@Override
	public final void remaining(BiConsumer<? super Piece, Integer > consumer) {
		remainingPieces.enumerate(consumer);
	}

	@Override
	public void notify(Move move) {
		if(move.piece.color() == color()) {
			if(move.from == null) {
				remainingPieces.takeOut(move.piece);
			}
		}
	}

	@Override
	public void notifyUndo(Move move) {
		if(move.piece.color() == color()) {
			if(move.from == null) {
				remainingPieces.putBack(move.piece);
			}
		}
	}

	@Override
	public IPlayer.Color color() { return color; } 

	public String toString() {
		if(color == IPlayer.Color.WHITE)
			return "White";
		else
			return "Black";
	}
}
