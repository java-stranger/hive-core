package hive.positions;

import java.util.function.BiConsumer;

import hive.engine.Coordinate;
import hive.pieces.Piece;
import hive.player.IPlayer.Color;

public abstract class ForwardPosition implements IPositionImpl {
	
	private final IPositionImpl positionImpl;
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ForwardPosition)) {
			return false;
		}
		ForwardPosition fwd = (ForwardPosition) obj;
		return this.positionImpl.equals(fwd.positionImpl);
	}
	
	public Piece move(Coordinate from, Coordinate to) {
		return positionImpl.move(from, to);
	}

	public void insert(Piece p, Coordinate at) {
		positionImpl.insert(p, at);
	}

	public Piece remove(Coordinate at) {
		return positionImpl.remove(at);
	}

	public Color switchTurn() {
		return positionImpl.switchTurn();
	}

	ForwardPosition(IPositionImpl impl) {
		positionImpl = impl;
	}

	public Piece getTopPieceAt(Coordinate pos) {
		return positionImpl.getTopPieceAt(pos);
	}

	public Piece getPieceAt(Coordinate pos, int below) {
		return positionImpl.getPieceAt(pos, below);
	}

	public boolean isFree(Coordinate pos) {
		return positionImpl.isFree(pos);
	}

	public int numTopPieces() {
		return positionImpl.numTopPieces();
	}

	public Color nextTurn() {
		return positionImpl.nextTurn();
	}

	public void forEach(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		positionImpl.forEach(consumer);
	}

	public void forEachVisible(BiConsumer<? super Coordinate, ? super Piece> consumer) {
		positionImpl.forEachVisible(consumer);
	}

	public Coordinate getAnyPoint() {
		return positionImpl.getAnyPoint();
	}
	
	
}
