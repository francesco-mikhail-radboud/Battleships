package io.github.spl.ships; 

import java.util.List; 

/**
 * TODO description
 */
public  class  ShipTemplate {
	
	private final String name;

	
	private List<Coordinate> coordinateList;

	
	
	public ShipTemplate(String name, List<Coordinate> coordinateList) {
		this.name = name;
		this.coordinateList = coordinateList;
	}

	
	
	public String getName() {
		return name;
	}

	
	
	public List<Coordinate> getCoordinates() {
		return coordinateList;
	}


}
