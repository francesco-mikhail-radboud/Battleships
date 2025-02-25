package io.github.spl.game;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.exceptions.GameViewException;
import io.github.spl.game.actions.*;
import io.github.spl.player.LocalPlayer;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.Ship;
import io.github.spl.ships.ShipCoordinate;
import io.github.spl.ships.ShipTemplate;

public abstract class GameView {	
	public static GameType createGameType(int ships, int dimensionX, int dimensionY) {
		GameType gameType = new GameType(new Dimension(dimensionX, dimensionY), createBasicFleet());
		return gameType;
	}
}