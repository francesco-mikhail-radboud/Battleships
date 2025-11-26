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

/**
 * TODO description
 */
public abstract class GameView {
	
	protected ConcurrentLinkedQueue<GameAction> gameActions;
	protected Game game;
	protected String USERNAME;
	protected boolean IS_HUMAN = true;
	protected int BOARD_DIMENSION_X = 10;
	protected int BOARD_DIMENSION_Y = 10;
	
	public GameView(String[] args) {
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		if (args.length < 2) {
			throw new GameViewException("Expected the username as a first argument, "
					+ "and player type (\"human\"/\"ai\") as a second argument!");
		}
		this.USERNAME = args[0];
		if (!args[1].equals("human") && !args[1].equals("ai")) {
			throw new GameViewException("Incorrect player type! "
					+ "Provide it as a second argument. "
					+ "Valid options: \"human\"/\"ai\"");
		}
		if (args[1].equals("human")) {
			this.IS_HUMAN = true;
		} else {
			this.IS_HUMAN = false;
		}
		args = Arrays.copyOfRange(args, 2, args.length);
	}

	public void run() {
		Thread thread = new Thread() {
			public void run() {
				game.play();
			}
		};

		thread.start();

		GameAction action = null;
		while (!(action instanceof GameWin || action instanceof ConnectivityError)) {
			if (!gameActions.isEmpty()) {
				action = gameActions.poll();
				processGameAction(action);
			}
		}

	}

	private void processGameAction(GameAction action) {
		if (action instanceof RequestCoordinates) {
			processRequestCoordinates((RequestCoordinates) action);
		} else if (action instanceof Hit) {
			processHit((Hit) action);
		} else if (action instanceof Damage) {
			processDamage((Damage) action);
		} else if (action instanceof Miss) {
			processMiss((Miss) action);
		} else if (action instanceof Sinkage) {
			processSinkage((Sinkage) action);
		} else if (action instanceof GameWin) {
			processGameWin((GameWin) action);
		} else if (action instanceof GameTick) {
			processGameTick((GameTick) action);
		} else if (action instanceof Setup) {
			processSetup((Setup) action);
		} else if (action instanceof ConnectivityError) {
			processConnectivityError((ConnectivityError) action);
		}
	}

	protected void processRequestCoordinates(RequestCoordinates action) {}
	
	protected void processHit(Hit action) {}

	protected void processDamage(Damage action) {}

	protected void processMiss(Miss action) {}

	protected void processSinkage(Sinkage action) {}

	protected void processGameWin(GameWin action) {}

	protected void processGameTick(GameTick action) {}

	protected void processSetup(Setup action) {}
	
	protected void processConnectivityError(ConnectivityError action) {}
	
	public void addGameAction(GameAction action) {
		gameActions.add(action);
	}
	
	public ConcurrentLinkedQueue<GameAction> getGameActions() {
		return gameActions;
	}
	
    public Game getGame() {
    	return game;
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
    
    public static GameType createBasicGameType(int dimensionX, int dimensionY) {
        return new GameType(new Dimension(dimensionX, dimensionY), createBasicFleet());
    }
}