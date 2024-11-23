package io.github.spl.protocol;

import io.github.spl.ships.Coordinate;

/**
 * TODO description
 */
public class RequestHit implements Command{
	
	private final Coordinate coordinate;
	
	public RequestHit(int x, int y){
		coordiante = new Coordinate(x, y);
	}

	@Override
	public String serilize(){
		return "C:" + coordiante.getX() + ";" + coordinate.getY() + ".";
	}
}