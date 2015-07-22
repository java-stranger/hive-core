package hive.positions;

public class CheckedPositionTraits extends PositionTraits {
	
	public IPositionChecker createPositionChecker() {
		return new PositionChecker();
	}
	
}
