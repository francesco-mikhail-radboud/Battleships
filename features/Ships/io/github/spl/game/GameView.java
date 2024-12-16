package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.exceptions.GameViewException;
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
		if (args.length < 1) {
			throw new GameViewException("Please provide the game difficulty (1-3).");
		}
		int difficulty = 0;
		try {
			difficulty = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new GameViewException("Unable to parse the game difficulty level. "
					+ "Provide it as an integer (1-3).");
		}
		if (difficulty < 1 || difficulty > 3) {
			throw new GameViewException("Invalid game difficulty! Please provide the game difficulty (1-3).");
		}
		this.game = new Game(createGameType(difficulty, BOARD_DIMENSION_X, BOARD_DIMENSION_Y), this);
	}
	
	public static GameType createGameType(int difficulty, int dimensionX, int dimensionY) {
		GameType gameType = null;
		switch (difficulty) {
			case 1:
				gameType = new GameType(new Dimension(dimensionX, dimensionY), createEasyFleet());
				break;
			case 2:
				gameType = new GameType(new Dimension(dimensionX, dimensionY), createBasicFleet());
				break;
			case 3:
				gameType = new GameType(new Dimension(dimensionX, dimensionY), createDifficultFleet());
				break;
		}
		
		return gameType;
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