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

public abstract class LocalPlayer implements Player {

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