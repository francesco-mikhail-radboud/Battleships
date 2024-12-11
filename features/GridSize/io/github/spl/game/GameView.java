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

	public GameView() {

		this.game = new Game(createBasicGameType(30, 30), this);
	}

	public static GameType createBasicGameType(int d1, int d2) {
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		coordinateList.add(new Coordinate(0, 0));

		ShipTemplate ship1 = new ShipTemplate("basic", coordinateList);

		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();
		shipTemplates.add(ship1);

		//GameType standartType = new GameType(new Dimension(10, 10), shipTemplates);
		GameType standardType = new GameType(new Dimension(d1, d2), createBasicFleet());
		return standardType;
	}


}