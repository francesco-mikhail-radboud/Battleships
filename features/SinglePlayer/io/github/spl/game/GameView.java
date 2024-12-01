package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.GameAction;
import io.github.spl.ships.*;
import io.github.spl.player.*;
import io.github.spl.game.*;
import java.util.Random;

/**
 * TODO description
 */
public abstract class GameView {
    public void run() {
        HumanPlayer player1 = new HumanPlayer("Human", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
        //humanPlayer.addShip(game.getGameType().getTemplates().get(0), new Coordinate(1, 1), 0);
        //setupRandomFleet(player1, game.getGameType().getTemplates());

        AIPlayer player2 = new AIPlayer("AI", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
        // aiPlayer.addShip(game.getGameType().getTemplates().get(0), new Coordinate(2, 2), 0);
        // setupRandomFleet(player2, game.getGameType().getTemplates());
		
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        
        original();
	}

    private static final Random random = new Random();

    public static void setupRandomFleet(LocalPlayer player, List<ShipTemplate> shipTemplates) {
    	GameGrid gameGrid = player.getGameGrid();
        List<Ship> ships = new ArrayList<Ship>();

        for (ShipTemplate template : shipTemplates) {
            boolean placed = false;

            while (!placed) {
                int x = random.nextInt(gameGrid.getDimension().getWidth());
                int y = random.nextInt(gameGrid.getDimension().getHeight());
                Coordinate startingCoordinate = new Coordinate(x, y);
                int timesRotated = random.nextInt(4); // rotation

                Ship ship = new Ship(template, startingCoordinate, timesRotated);

                if (canPlaceShip(ship, ships, gameGrid)) {
                    ships.add(ship);
                    player.addShip(template, startingCoordinate, timesRotated);
                    placed = true;
                }
            }
        }
    }

    public static boolean canPlaceShip(Ship ship, List<Ship> existingShips, GameGrid grid) {
        for (ShipCoordinate coord : ship.getShipCoordinates()) {
            if (coord.getX() < 0 || coord.getX() >= grid.getDimension().getWidth() ||
                coord.getY() < 0 || coord.getY() >= grid.getDimension().getHeight()) {
                return false;
            }

            // Check for overlap with existing ships
            for (Ship existingShip : existingShips) {
                if (existingShip.getShipCoordinates().contains(coord)) {
                    return false;
                }
            }
        }
        return true;
    }
}