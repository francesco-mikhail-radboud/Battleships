package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.GameAction;
import io.github.spl.player.AIPlayer;
import io.github.spl.player.HumanPlayer;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.ShipTemplate;

public abstract class GameView {
	public GameView(String[] args) {
		this.game = new Game(createBasicGameType(BOARD_DIMENSION_X, BOARD_DIMENSION_Y), this);
	}

    private static List<Coordinate> createCoordinates(int length) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < length; i++) {
            coordinates.add(new Coordinate(0, i));
        }
        return coordinates;
    }
}