package io.github.spl.player;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;

import io.github.spl.game.GameGrid;
import io.github.spl.game.GameView;
import io.github.spl.game.actions.RequestCoordinates;
import io.github.spl.game.actions.Sinkage;
import io.github.spl.player.Player;
import io.github.spl.protocol.*;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.*;

public class AIPlayer extends LocalPlayer {

    private int[][] probabilityMap;
    private int gridWidth;
    private int gridHeight;
    private List<ShipTemplate> remainingOpponentShips;
    private Queue<Coordinate> targetQueue;
    private String lastHitDirection;



    public AIPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView, int port) {
        super(name, ships, gameGrid, gameView, port);

        this.gridWidth = gameGrid.getDimension().getWidth();
        this.gridHeight = gameGrid.getDimension().getHeight();
        this.probabilityMap = new int[gridWidth][gridHeight];
        this.remainingOpponentShips = new ArrayList<ShipTemplate>();
        this.targetQueue = new LinkedList<Coordinate>();
        this.lastHitDirection = null;
        initializeProbabilityMap();
    }

    private void initializeProbabilityMap() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                probabilityMap[x][y] = 0;
            }
        }
    }

    public void initializeRemainingOpponentShips(List<ShipTemplate> shipTemplates) {
        remainingOpponentShips.addAll(shipTemplates);
    }

    public void updateRemainingOpponentShips(Sinkage action) {
        String sunkShipName = action.getShipName();
        for (ShipTemplate shipTemplate : remainingOpponentShips) {
        	if (shipTemplate.getName().equals(sunkShipName)) {
        		remainingOpponentShips.remove(shipTemplate);
        		break;
        	}
        }
        targetQueue.clear();
    }

    @Override
    public ResponseCoordinate selectCoordinate() {
    	
    	gameView.addGameAction(new RequestCoordinates(this));
		
		Command command = null;
		while (!(command instanceof ResponseCoordinate)) {
			if (!commandQueue.isEmpty()) {
				command = commandQueue.poll();
			}
		}
    	
        if (!targetQueue.isEmpty()) {
            Coordinate nextTarget = targetQueue.poll();
            
            ResponseCoordinate responseCoordinate = new ResponseCoordinate(gameView.getGame().getStep(), nextTarget.getX(), nextTarget.getY());
    		LAST_RESPONSE_COORDINATE.add(responseCoordinate);
            return responseCoordinate;
        }

        updateProbabilityMap();
        Coordinate bestCoordinate = getHighestProbabilityCoordinate();
        
        ResponseCoordinate responseCoordinate = new ResponseCoordinate(gameView.getGame().getStep(), bestCoordinate.getX(), bestCoordinate.getY());
        LAST_RESPONSE_COORDINATE.add(responseCoordinate);
        return responseCoordinate;
    }
    
    private void updateProbabilityMap() {
        initializeProbabilityMap(); // Reset Probability Map

        // Consider all possible positions for each remaining ship
        for (ShipTemplate ship : remainingOpponentShips) {
            int shipLength = ship.getCoordinates().size();

            // Horizontal
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y <= gridHeight - shipLength; y++) {
                    if (canPlaceShip(x, y, shipLength, true)) {
                        for (int i = 0; i < shipLength; i++) {
                            probabilityMap[x][y + i]++;
                        }
                    }
                }
            }

            // Vertical
            for (int x = 0; x <= gridWidth - shipLength; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    if (canPlaceShip(x, y, shipLength, false)) {
                        for (int i = 0; i < shipLength; i++) {
                            probabilityMap[x + i][y]++;
                        }
                    }
                }
            }
        }
    }

    private boolean canPlaceShip(int x, int y, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int checkX = horizontal ? x : x + i;
            int checkY = horizontal ? y + i : y;

            if (checkX >= gridWidth || checkY >= gridHeight ||
                    !checkListHits(checkX, checkY, gameGrid)) {
                return false;
            }
        }
        return true;
    }

    public Coordinate getHighestProbabilityCoordinate() {
        int maxProbability = -1;
        Coordinate bestCoordinate = null;

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (probabilityMap[x][y] > maxProbability && checkListHits(x, y, gameGrid)) {
                    maxProbability = probabilityMap[x][y];
                    bestCoordinate = new Coordinate(x, y);
                }
            }
        }
        
        if (maxProbability == 0) {
        	bestCoordinate = randomCoordToHit();
        }
        
        return bestCoordinate;
    }
    
    public Coordinate randomCoordToHit() {
        int x = getRandomHit(gameGrid.getDimension().getWidth());
        int y = getRandomHit(gameGrid.getDimension().getHeight());
        return new Coordinate(x, y);
    }

    public int getRandomHit(int maxDimension) {
		return (int)(Math.random() * maxDimension);
	}

    public void addAdjacentCoordinates(Coordinate hit, String shipName) {
        int x = hit.getX();
        int y = hit.getY();

        // If we have a direction to follow, let us continue
        if (!targetQueue.isEmpty() && lastHitDirection != null) {
            Coordinate nextTarget = getNextTargetInDirection(hit, lastHitDirection);
            if (nextTarget != null && checkListHits(nextTarget.getX(), nextTarget.getY(), gameGrid)) {
                targetQueue.add(nextTarget);
                return;
            } else {
                // If we cannot continue, we reverse direction
                lastHitDirection = reverseDirection(lastHitDirection);
                Coordinate reversedTarget = getNextTargetInDirection(hit, lastHitDirection);
                if (reversedTarget != null && checkListHits(reversedTarget.getX(), reversedTarget.getY(), gameGrid)) {
                    targetQueue.add(reversedTarget);
                    return;
                } else {
                    // If there are no longer any valid coordinates, we reset
                    targetQueue.clear();
                    lastHitDirection = null;
                }
            }
            return;
        }

        // Add neighbouring co-ordinates to start the search
        targetQueue.clear();
        if (x > 0 && checkListHits(x - 1, y, gameGrid)) {
            targetQueue.add(new Coordinate(x - 1, y)); // Nord
        }
        if (x < gridWidth - 1 && checkListHits(x + 1, y, gameGrid)) {
            targetQueue.add(new Coordinate(x + 1, y)); // Sud
        }
        if (y > 0 && checkListHits(x, y - 1, gameGrid)) {
            targetQueue.add(new Coordinate(x, y - 1)); // Ovest
        }
        if (y < gridHeight - 1 && checkListHits(x, y + 1, gameGrid)) {
            targetQueue.add(new Coordinate(x, y + 1)); // Est
        }

        // Determines the direction based on other shots in the same ship
        for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
            if (coord.getIsHit() && coord.getShipName().equals(shipName)) {
                if (coord.getX() == x) {
                    lastHitDirection = "HORIZONTAL";
                } else if (coord.getY() == y) {
                    lastHitDirection = "VERTICAL";
                }
                break;
            }
        }
    }

    private Coordinate getNextTargetInDirection(Coordinate current, String direction) {
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case "HORIZONTAL":
                if (y < gridWidth - 1 && checkListHits(x, y + 1, gameGrid)) {
                    return new Coordinate(x, y + 1); // Right
                }
                break;
            case "HORIZONTAL_REVERSE":
                if (y > 0 && checkListHits(x, y - 1, gameGrid)) {
                    return new Coordinate(x, y - 1); // Left
                }
                break;
            case "VERTICAL":
                if (x < gridHeight - 1 && checkListHits(x + 1, y, gameGrid)) {
                    return new Coordinate(x + 1, y); // Under
                }
                break;
            case "VERTICAL_REVERSE":
                if (x > 0 && checkListHits(x - 1, y, gameGrid)) {
                    return new Coordinate(x - 1, y); // Over
                }
                break;
        }
        return null;
    }

    private String reverseDirection(String currentDirection) {
        switch (currentDirection) {
            case "HORIZONTAL":
                return "HORIZONTAL_REVERSE";
            case "HORIZONTAL_REVERSE":
                return "HORIZONTAL";
            case "VERTICAL":
                return "VERTICAL_REVERSE";
            case "VERTICAL_REVERSE":
                return "VERTICAL";
            default:
                return null;
        }
    }
}
