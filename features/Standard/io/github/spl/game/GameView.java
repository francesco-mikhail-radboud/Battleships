package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.GameAction;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.ShipTemplate;

/**
 * TODO description
 */
public abstract class GameView {

	public static GameType createBasicGameType() {
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		coordinateList.add(new Coordinate(0, 0));

		ShipTemplate ship1 = new ShipTemplate("basic", coordinateList);

		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();
		shipTemplates.add(ship1);

		GameType standartType = new GameType(new Dimension(10, 10), shipTemplates);
		return standartType;
	}

	public GameView() {
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		this.game = new Game(createBasicGameType(), this);
	}

	public void run() {
		
	}
}