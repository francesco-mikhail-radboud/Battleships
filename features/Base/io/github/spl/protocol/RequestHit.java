package io.github.spl.protocol;

import io.github.spl.ships.Coordinate;

/**
 * TODO description
 */
public class RequestHit implements Command {
	
	private final Coordinate coordinate;
	
	public RequestHit(int x, int y) {
		this.coordinate = new Coordinate(x, y);
	}

	@Override
	public String serialize() {
		return "C:" + coordinate.getX() + ";" + coordinate.getY() + ".";
	}
}