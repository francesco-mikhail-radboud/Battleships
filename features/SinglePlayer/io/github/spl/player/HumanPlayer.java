package io.github.spl.player;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import io.github.spl.game.GameGrid;
import io.github.spl.game.GameView;
import io.github.spl.game.actions.*;
import io.github.spl.player.Player;
import io.github.spl.protocol.*;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.Ship;

public class HumanPlayer extends LocalPlayer implements Player {

    private GameView gameView;
    
    private ConcurrentLinkedQueue<Command> commandQueue;

    public HumanPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView) {
        super(name, ships, gameGrid);
        this.gameView = gameView;
        this.commandQueue = new ConcurrentLinkedQueue<Command>();
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
		gameView.getGameActions().add(new RequestCoordinates(this));
		
		Command command = null;
		while (!(command instanceof ResponseCoordinate)) {
			if (!commandQueue.isEmpty()) {
				command = commandQueue.poll();
			}
		}

		return (ResponseCoordinate) command;
    }
    
    public String getName() {
		return name;
	}
    
    public ConcurrentLinkedQueue<Command> getCommandQueue() {
    	return commandQueue;
    }
}
