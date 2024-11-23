package io.github.spl.ships; 

/**
 * TODO description
 */
public  class  ShipCoordinate {
	
	private final int x;

	 //row
	private final int y;

	 //column
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


}
