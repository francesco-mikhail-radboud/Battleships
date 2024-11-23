package io.github.spl.ships; 

import java.util.ArrayList; 
import java.util.List; 

import io.github.spl.game.GameGrid; 

/**
 * TODO description
 */
public  class  Ship {
	
	
	private List<ShipCoordinate> coordinates;

	
	
	private String name;

	
	
	public Ship(ShipTemplate template, Coordinate onGrid, int timesRotated) {
		this.coordinates = new ArrayList<ShipCoordinate>();
		
		for (Coordinate coord : template.getCoordinates()) {
			if (timesRotated % 4 == 0) {
				coordinates.add(new ShipCoordinate(onGrid.getX() + coord.getX(), onGrid.getY() + coord.getY()));
			} else if (timesRotated % 4 == 1) {
				coordinates.add(new ShipCoordinate(onGrid.getX() - coord.getY(), onGrid.getY() + coord.getX()));
			} else if (timesRotated % 4 == 2) {
				coordinates.add(new ShipCoordinate(onGrid.getX() - coord.getX(), onGrid.getY() - coord.getY()));
			} else if (timesRotated % 4 == 3) {
				coordinates.add(new ShipCoordinate(onGrid.getX() + coord.getY(), onGrid.getY() - coord.getX()));
			}
		}
		
		this.name = template.getName();
	}


}
