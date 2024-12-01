package io.github.spl.player;

import java.util.List;
import java.util.ArrayList;

import io.github.spl.game.GameGrid;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.Ship;
import io.github.spl.ships.ShipTemplate;

public abstract class LocalPlayer {

    protected String name;

    protected List<Ship> ships;

    protected GameGrid gameGrid;

    public LocalPlayer(String name, List<Ship> ships, GameGrid gameGrid) {
        this.name = name;
        this.ships = new ArrayList<Ship>(ships);;
        this.gameGrid = gameGrid;
    }

    public boolean addShip(ShipTemplate shipTemplate, Coordinate coordinate, int timesRotated) {
        Ship ship = new Ship(shipTemplate, coordinate, timesRotated);
        ships.add(ship);
        return true;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }
}