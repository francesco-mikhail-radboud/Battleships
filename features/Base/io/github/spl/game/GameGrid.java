package io.github.spl.game;

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
	
	public getDimension(){
		return dimension;
	}
	
	public getListOfCoordsHit(){
		return listOfCoordsHit;
	}
}