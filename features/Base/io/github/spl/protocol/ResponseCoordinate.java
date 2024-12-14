package io.github.spl.protocol;

import io.github.spl.ships.Coordinate;

/**
 * TODO description
 */
public class ResponseCoordinate extends Command {
	
	private final Coordinate coordinate;
	
	public ResponseCoordinate(int id, int x, int y) {
		super(id);
		this.coordinate = new Coordinate(x, y);
	}

	@Override
	public String serialize() {
		return super.serialize() + "C:" + coordinate.getX() + ";" + coordinate.getY() + ".";
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseCoordinate)) {
        	return false;
        }

        return this.coordinate.equals(((ResponseCoordinate) other).coordinate) && 
        		super.equals(other);
    }
}