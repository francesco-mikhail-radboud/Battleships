package io.github.spl.game;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.spl.exceptions.GameViewException;
import io.github.spl.game.actions.*;
import io.github.spl.ships.*;
import io.github.spl.player.*;

public abstract class GameView {
	public GameView(String[] args) {
		if (args.length < 2) {
			throw new GameViewException("Please provide the game field sizes as two non-negative integers.");
		}
		int dimensionX = 0;
		int dimensionY = 0;
		try {
			dimensionX = Integer.parseInt(args[0]);
			dimensionY = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new GameViewException("Unable to parse the game field sizes. "
					+ "Provide them as non-negative numbers.");
		}
		if (dimensionX <= 0 || dimensionY <= 0) {
			throw new GameViewException("Please provide the game field sizes as two non-negative integers.");
		}
		this.BOARD_DIMENSION_X = dimensionX;
		this.BOARD_DIMENSION_Y = dimensionY;
		this.game = new Game(createGameType(1, BOARD_DIMENSION_X, BOARD_DIMENSION_Y), this);
		
		args = Arrays.copyOfRange(args, 2, args.length);
	}
}