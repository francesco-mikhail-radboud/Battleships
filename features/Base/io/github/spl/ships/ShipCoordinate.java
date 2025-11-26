package io.github.spl.ships;

public  class  ShipCoordinate {

	private final int x;

	//row
	private final int y;

	//column
	private boolean isHit;

	private String shipName;

	public ShipCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
		this.isHit = false;
		this.shipName = null;
	}

	public ShipCoordinate(int x, int y, String shipName) {
		this.x = x;
		this.y = y;
		this.isHit = false;
		this.shipName = shipName;
	}

	public ShipCoordinate(Coordinate c) {
		this(c.getX(), c.getY());
	}
	public ShipCoordinate(Coordinate c, String shipName) {
		this(c.getX(), c.getY(), shipName);
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

	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
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

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}