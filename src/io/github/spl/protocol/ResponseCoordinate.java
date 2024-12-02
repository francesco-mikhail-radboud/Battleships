package io.github.spl.protocol; 

import io.github.spl.ships.Coordinate; 

/**
 * TODO description
 */
public  class  ResponseCoordinate  implements Command {
	
	
	private final Coordinate coordinate;

	
	
	public ResponseCoordinate(int x, int y) {
		this.coordinate = new Coordinate(x, y);
	}

	

	@Override
	public String serialize() {
		return "C:" + coordinate.getX() + ";" + coordinate.getY() + ".";
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

        return this.coordinate.equals(((ResponseCoordinate) other).coordinate);
    }


}
