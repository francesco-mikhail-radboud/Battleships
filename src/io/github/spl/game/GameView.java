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
import io.github.spl.game.actions.GameAction; 
import io.github.spl.ships.*; 
import io.github.spl.player.*; 
import io.github.spl.game.*; 
import io.github.spl.player.AIPlayer; 
import io.github.spl.player.HumanPlayer; 

/**
 * TODO description
 */
public abstract   class  GameView {
	
	
	protected ConcurrentLinkedQueue<GameAction> gameActions;

	
	protected Game game;

	
	protected String USERNAME;

	
	protected boolean IS_HUMAN = true;

	
	protected int BOARD_DIMENSION_X = 10;

	
	protected int BOARD_DIMENSION_Y = 10;

	
	public GameView  (String[] args) {
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
	
		this.game = new Game(createBasicGameType(BOARD_DIMENSION_X, BOARD_DIMENSION_Y), this);
	
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

	

	 private void  run__wrappee__Base  () {
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

	
    public void run() {
    	LocalPlayer player1 = null;
    	if (IS_HUMAN) {
    		player1 = new HumanPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	} else {
    		player1 = new AIPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	}

        AIPlayer player2 = new AIPlayer("AI", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);

        game.setPlayer1(player1);
        game.setPlayer2(player2);
        
        run__wrappee__Base();
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

	

    private static List<Coordinate> createCoordinates(int length) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < length; i++) {
            coordinates.add(new Coordinate(0, i));
        }
        return coordinates;
    }

	
	
	public static GameType createGameType  (int difficulty, int dimensionX, int dimensionY) {
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
