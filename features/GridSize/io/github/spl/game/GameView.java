package io.github.spl.game;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.util.List;

import io.github.spl.game.actions.*;
import io.github.spl.ships.*;
import io.github.spl.player.*;

/**
 * TODO description
 */
public abstract class GameView {
	
	protected ConcurrentLinkedQueue<GameAction> gameActions;
	protected Game game;

	public GameView(String[] args) {

		if (args != null && args.length == 1){
			this.game = new Game(createGameType(Integer.parseInt(args[0]), 10, 10), this);
		} else if (args != null && args.length == 2){
			this.game = new Game(createGameType(-1, Integer.parseInt(args[0]), Integer.parseInt(args[1])), this);
		} else if (args != null && args.length == 3){
			this.game = new Game(createGameType(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])), this);
		} else {
			this.game = new Game(new GameType(new Dimension(10, 10), createBasicFleet()), this);
		}
	}

	public static GameType createGameType(int ships, int dimensionX, int dimensionY) {
		if (dimensionX > 0 && dimensionY > 0) {
			if (ships == 0) {
				return new GameType(new Dimension(dimensionX, dimensionY), createEasyFleet());
			} else if (ships == 1) {
				return new GameType(new Dimension(dimensionX, dimensionY), createBasicFleet());
			} else if (ships == 2) {
				return new GameType(new Dimension(dimensionX, dimensionY), createDifficultFleet());
			}
			return new GameType(new Dimension(dimensionX, dimensionY), createBasicFleet());
		}
		return new GameType(new Dimension(10, 10), createBasicFleet());
	}

}