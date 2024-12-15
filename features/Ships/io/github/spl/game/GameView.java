package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.GameAction;
import io.github.spl.player.AIPlayer;
import io.github.spl.player.HumanPlayer;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.ShipTemplate;

/**
 * TODO description
 */
public abstract class GameView {
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

	public static List<ShipTemplate> createEasyFleet() {
		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();

		// Destroyer: 1 ship, length 2
		shipTemplates.add(new ShipTemplate("Destroyer", createCoordinates(2)));


		// Submarine: 1 ship, length 3
		shipTemplates.add(new ShipTemplate("Submarine", createCoordinates(3)));


		// Cruiser: 1 ship, length 3
		shipTemplates.add(new ShipTemplate("Cruiser", createCoordinates(3)));


		// Battleship: 1 ship, length 4
		shipTemplates.add(new ShipTemplate("Battleship", createCoordinates(4)));


		// Carrier: 1 ship, length 5
		shipTemplates.add(new ShipTemplate("Carrier", createCoordinates(5)));

		return shipTemplates;
	}

	public static List<ShipTemplate> createDifficultFleet() {
		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();

		// Destroyer: 2 ship, length 2
		for (int i = 0; i < 2; i++) {
			List<Coordinate> coordinates = createCoordinates(2);
			shipTemplates.add(new ShipTemplate("Destroyer", coordinates));
		}

		// Submarine: 2 ship, length 3
		for (int i = 0; i < 2; i++) {
			List<Coordinate> coordinates = createCoordinates(3);
			shipTemplates.add(new ShipTemplate("Submarine", coordinates));
		}

		// Cruiser: 2 ship, length 3
		for (int i = 0; i < 2; i++) {
			List<Coordinate> coordinates = createCoordinates(3, 2);
			shipTemplates.add(new ShipTemplate("Cruiser", coordinates));
		}

		// Battleship: 2 ship, length 4
		for (int i = 0; i < 2; i++) {
			List<Coordinate> coordinates = createCoordinates(4);
			shipTemplates.add(new ShipTemplate("Battleship", coordinates));
		}

		// Carrier: 1 ship, length 5
		List<Coordinate> coordinates = createCoordinates(5);
		shipTemplates.add(new ShipTemplate("Carrier", coordinates));

		// BigCarrier: 1 ship, length 5
		shipTemplates.add(new ShipTemplate("BigCarrier", createCoordinates(5, 2)));

		return shipTemplates;
	}

	private static List<Coordinate> createCoordinates(int length, int height) {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < height; j++) {
				coordinates.add(new Coordinate(j, i));
			}
		}
		return coordinates;
	}
}