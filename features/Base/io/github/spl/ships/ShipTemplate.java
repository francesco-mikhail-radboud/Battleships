package io.github.spl.ships;

import java.util.List;

public class ShipTemplate {
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
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ShipTemplate)) {
        	return false;
        }
        
        ShipTemplate otherShipTemplate = (ShipTemplate) other;
        
        if (this.name == null) {
        	if (otherShipTemplate.name != null) {
        		return false;
        	}
        } 
        
        if (!this.name.equals(otherShipTemplate.name)) {
        	return false;
        }
        
        if (this.coordinateList.size() != otherShipTemplate.coordinateList.size()) {
			return false;
		}
		
		for (Coordinate c : coordinateList) {
			if (!otherShipTemplate.coordinateList.contains(c)) {
				return false;
			}
		}
        
        return true;
    }
}