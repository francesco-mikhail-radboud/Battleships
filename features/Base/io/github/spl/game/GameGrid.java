package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;

import io.github.spl.ships.ShipCoordinate;

/**
 * TODO description
 */
public class GameGrid {
	
	private final Dimension dimension;
	private List<ShipCoordinate> listOfCoordsHit;
	
	public GameGrid(Dimension dimension) {
		this.dimension = dimension;
		listOfCoordsHit = new ArrayList<ShipCoordinate>();
	}
	
	public Dimension getDimension(){
		return dimension;
	}
	
	public List<ShipCoordinate> getListOfCoordsHit(){
		return listOfCoordsHit;
	}
}