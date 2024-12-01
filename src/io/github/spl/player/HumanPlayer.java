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
import io.github.spl.game.actions.RequestCoordinates; 

public  class  HumanPlayer  extends LocalPlayer {
	

    public HumanPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView) {
        super(name, ships, gameGrid, gameView);
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

		return (ResponseCoordinate) command;
    }


}
