package hive.positions;

public class PositionTraits {

	public IPositionImpl createPositionImpl() {
		return new PositionImpl();
	}
	
	public IPositionChecker createPositionChecker() {
		return new NoopPositionChecker();
	}

}
