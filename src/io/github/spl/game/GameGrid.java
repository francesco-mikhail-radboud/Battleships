package io.github.spl.game; 

import io.github.spl.ships.*; 
import java.util.List; 
import java.util.ArrayList; 

/**
 * TODO description
 */
public  class  GameGrid {
	
	private final Dimension dimension;

	
	private List<ShipCoordinate> listOfCoordsHit;

	
	
	public GameGrid(Dimension dimension) {
		this.dimension = dimension;
		this.listOfCoordsHit = new ArrayList<ShipCoordinate>();
	}

	
	
	public Dimension getDimension(){
		return dimension;
	}

	
	
	public List<ShipCoordinate> getListOfCoordsHit(){
		return listOfCoordsHit;
	}


}
