package io.github.spl.player; 

import java.util.List; 
import java.util.ArrayList; 

import io.github.spl.game.GameGrid; 
import io.github.spl.game.GameView; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.Ship; 
import io.github.spl.ships.ShipCoordinate; 
import io.github.spl.ships.ShipTemplate; 
import io.github.spl.protocol.*; 
import java.util.concurrent.ConcurrentLinkedQueue; 
import io.github.spl.protocol.ResponseHit.ResponseHitOption; 
import io.github.spl.game.actions.*; 

public abstract   class  LocalPlayer  implements Player {
	

    protected String name;

	
    protected List<Ship> ships;

	
    protected GameGrid gameGrid;

	
    protected ConcurrentLinkedQueue<Command> commandQueue;

	
    protected GameView gameView;

	

    public LocalPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView) {
        this.name = name;
        this.ships = new ArrayList<Ship>(ships);;
        this.gameGrid = gameGrid;
        this.commandQueue = new ConcurrentLinkedQueue<Command>();
        this.gameView = gameView;
    }

	
    
    public static boolean checkListHits(int x, int y, GameGrid gameGrid) {
        for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
            if (coord.getX() == x && coord.getY() == y) {
                return false;
            }
        }
        return true;
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

	

	public ResponseCoordinate selectCoordinate() {
		return null;
	}

	

	public String getName() {
        return name;
    }

	

    public ConcurrentLinkedQueue<Command> getCommandQueue() {
    	return commandQueue;
    }

	

    public ResponseHit hit(Coordinate coordinate) {
        for (Ship ship : ships) {
            if (ship.hit(coordinate)) {
                if (ship.isSunk()) {
                    return new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.SINK, ship.getName());
                } else {
                    return new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.HIT, ship.getName());
                }
            }
        }

        return new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.MISS, null);
    }

	

    public ResponseGameLost isGameLost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return new ResponseGameLost(gameView.getGame().getStep(), false);
            }
        }

        return new ResponseGameLost(gameView.getGame().getStep(), true);
    }

	

    public ResponseSetup setup() {
        gameView.addGameAction(new Setup(this));

        Command command = null;
		while (!(command instanceof ResponseSetup)) {
			if (!commandQueue.isEmpty()) {
				command = commandQueue.poll();
			}
		}

        return (ResponseSetup) command;
    }


}
