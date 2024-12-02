package io.github.spl.ships;

/**
 * TODO description
 */
public class Coordinate {
	private final int x; //row
	private final int y; //column
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Coordinate)) {
        	return false;
        }
        
        Coordinate otherCoordinate = (Coordinate) other;

        return this.x == otherCoordinate.x && this.y == otherCoordinate.y;
    }
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
}