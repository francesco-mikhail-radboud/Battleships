package io.github.spl.ships; 

/**
 * TODO description
 */
public  class  Coordinate {
	
	private final int x;

	 //row
	private final int y;

	 //column
	
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


}
