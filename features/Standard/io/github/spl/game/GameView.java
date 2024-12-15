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
 
	public static GameType createBasicGameType() {
        return new GameType(new Dimension(10, 10), createBasicFleet());
	}

	public GameView(String[] args) {
		this.game = new Game(createBasicGameType(), this);
	}

	public static List<ShipTemplate> createBasicFleet() {
        List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();

        // Submarine: 3 ships, length 2
        for (int i = 0; i < 3; i++) {
            List<Coordinate> coordinates = createCoordinates(2);
            shipTemplates.add(new ShipTemplate("Submarine", coordinates));
        }

        // Destroyer: 2 ships, length 3
        for (int i = 0; i < 2; i++) {
            List<Coordinate> coordinates = createCoordinates(3);
            shipTemplates.add(new ShipTemplate("Destroyer", coordinates));
        }

        // Battleship: 2 ships, length 4
        for (int i = 0; i < 2; i++) {
            List<Coordinate> coordinates = createCoordinates(4);
            shipTemplates.add(new ShipTemplate("Battleship", coordinates));
        }

        // Carrier: 1 ship, length 5
        List<Coordinate> coordinates = createCoordinates(5); 
        shipTemplates.add(new ShipTemplate("Carrier", coordinates));

        return shipTemplates;
    }

    private static List<Coordinate> createCoordinates(int length) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < length; i++) {
            coordinates.add(new Coordinate(0, i));
        }
        return coordinates;
    }
}