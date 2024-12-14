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

	private ShipTemplate template;

	private boolean isSunk;

	public Ship(ShipTemplate template, Coordinate onGrid, int timesRotated) {
		this.coordinates = new ArrayList<ShipCoordinate>();

		for (Coordinate coord : template.getCoordinates()) {
			if (timesRotated % 4 == 0) {
				coordinates.add(new ShipCoordinate(onGrid.getX() + coord.getX(), onGrid.getY() + coord.getY(), template.getName()));
			} else if (timesRotated % 4 == 1) {
				coordinates.add(new ShipCoordinate(onGrid.getX() - coord.getY(), onGrid.getY() + coord.getX(), template.getName()));
			} else if (timesRotated % 4 == 2) {
				coordinates.add(new ShipCoordinate(onGrid.getX() - coord.getX(), onGrid.getY() - coord.getY(), template.getName()));
			} else if (timesRotated % 4 == 3) {
				coordinates.add(new ShipCoordinate(onGrid.getX() + coord.getY(), onGrid.getY() - coord.getX(), template.getName()));
			}
		}

		this.name = template.getName();

		this.isSunk = false;

		this.template = template;
	}

	public boolean hit(Coordinate coordinate) {
		for (ShipCoordinate c : coordinates) {
			if (c.getX() == coordinate.getX() && c.getY() == coordinate.getY()) {
				c.setIsHit(true);
				return true;
			}
		}

		return false;
	}

	public List<ShipCoordinate> getShipCoordinates() {
		return coordinates;
	}

	public String getName() {
		return name;
	}

	public boolean isSunk() {
		if (!isSunk) {
			for (ShipCoordinate c : coordinates) {
				if (!c.getIsHit()) {
					return false;
				}
			}
			isSunk = true;
			return isSunk;
		} else {
			return isSunk;
		}
	}

	public ShipTemplate getShipTemplate() {
		return template;
	}
}