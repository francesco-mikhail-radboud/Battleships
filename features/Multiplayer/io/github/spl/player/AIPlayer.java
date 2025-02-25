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
import io.github.spl.ships.*;

public class AIPlayer extends LocalPlayer {
	
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
}
