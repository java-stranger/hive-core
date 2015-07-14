package main.java.hive.view;

import main.java.hive.engine.Coordinate;

public class TileGrid {
	
	private final float tileSize;
	private final static float Sqrt3 = (float)Math.sqrt(3.);
	
	private final float offX, offY;

	public TileGrid(float tileSize) {
		this(tileSize, 0, 0);
	}
	
	public TileGrid(float tileSize, float offX, float offY) {
		this.tileSize = tileSize;
		this.offX = offX;
		this.offY = offY;
	}
	
	public float pixelX(Coordinate c) {
		final float coeff = tileSize * 3.f/2; 
		return coeff * c.x() + offX;
	}
	
	public float pixelY(Coordinate c) {
		final float coeff = tileSize * Sqrt3 * 0.5f;
	    return (float) (coeff * (c.y() * 2 + c.x())) + offY;
	}
	
	public Coordinate grid(float pixelX, float pixelY) {
		pixelX -= offX;
		pixelY -= offY;
	    float x = pixelX * 2.f / 3 / tileSize;
	    float y = (pixelY * Sqrt3 - pixelX) / (tileSize * 3.f);
	    int rx = Math.round(x);
	    int ry = Math.round(y);
	    int rz = Math.round(-x-y);
	    if(Math.abs(ry-y) >= Math.abs(rx-x) && Math.abs(ry-y) >= Math.abs(rz+x+y)) {
    		ry = -rx-rz;
	    } else if(Math.abs(rx-x) >= Math.abs(rz+x+y)) {	    	
    		rx = -ry-rz;
    	} else {
    		rz = -rx-ry;
    	}
	    
	    return Coordinate.axial(rx, ry);
	}

}
