package hive.pieces;

public enum PieceType {
	QUEEN("Queen"),
	SPIDER("Spider"),
	BUG("Bug"),
	GRASSHOPER("Grasshoper"),
	ANT("Ant");
	
	private final String value;
	PieceType(String s) { value = s; }
	
	public String toString() {
		return value;
	}
}
