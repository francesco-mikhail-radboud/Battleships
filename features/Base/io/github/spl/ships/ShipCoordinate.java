package io.github.spl.ships;

/**
 * TODO description
 */
public class ShipCoordinate {
	private final int x; //row
	private final int y; //column
	private boolean isHit;
	
	public ShipCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
		this.isHit = false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean getIsHit(){
		return isHit;
	}
	
	public void setIsHit(boolean isHit){
		this.isHit = isHit;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipCoordinate coordinate = (ShipCoordinate) o;
        return x == coordinate.x && y == coordinate.y;
    }

	@Override
    public int hashCode() {
        return 31 * x + y;
    }
	
}