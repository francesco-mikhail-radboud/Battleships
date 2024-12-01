package io.github.spl.player; 

import java.util.List; 
import java.io.BufferedInputStream; 
import java.io.BufferedReader; 
import java.io.InputStream; 
import java.io.OutputStream; 

import io.github.spl.game.GameGrid; 
import io.github.spl.game.GameView; 
import io.github.spl.game.actions.RequestCoordinates; 
import io.github.spl.player.Player; 
import io.github.spl.protocol.*; 
import io.github.spl.protocol.ResponseHit.ResponseHitOption; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.*; 

public  class  AIPlayer  extends LocalPlayer  implements Player {
	

    private GameView gameView;

	

    public AIPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView) {
        super(name, ships, gameGrid);
		this.gameView = gameView;
	}

	
    
    public ResponseHit hit(Coordinate coordinate) {
        for (Ship ship : ships) {
            if (ship.hit(coordinate)) {
                if (ship.isSunk()) {
                    return new ResponseHit(ResponseHitOption.SINK, ship.getName());
                } else {
                    return new ResponseHit(ResponseHitOption.HIT, ship.getName());
                }
            }
        }

        return new ResponseHit(ResponseHitOption.MISS, null);
    }

	

    public ResponseGameLost isGameLost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return new ResponseGameLost(false);
            }
        }

        return new ResponseGameLost(true);
    }

	

    public ResponseCoordinate selectCoordinate() {
        Coordinate xy;
		do {
			xy = coordToHit();
		} while (!checkListHits(xy.getX(), xy.getY(), gameGrid));

        return new ResponseCoordinate(xy.getX(), xy.getY());
    }

	

    public Coordinate coordToHit() {
        int x = getRandomHit(gameGrid.getDimension().getWidth());
        int y = getRandomHit(gameGrid.getDimension().getHeight());
        return new Coordinate(x, y);
    }

	

    public int getRandomHit(int maxDimension) {
		return (int)(Math.random() * maxDimension);
	}

	

    public boolean checkListHits(int x, int y, GameGrid gameGrid) {
		for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
			if (coord.getX() == x && coord.getY() == y) {
				return false;
			}
		}
		return true;
	}

	
    
    public String getName() {
    	return name;
    }


}
