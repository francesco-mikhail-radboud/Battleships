package io.github.spl.game; 

import java.io.BufferedOutputStream; 
import java.io.ByteArrayOutputStream; 
import java.io.OutputStream; 
import java.util.concurrent.ConcurrentLinkedQueue; 

import io.github.spl.game.actions.*; 

import java.util.ArrayList; 
import java.util.List; 

import io.github.spl.game.actions.GameAction; 
import io.github.spl.ships.*; 
import io.github.spl.player.*; 
import io.github.spl.game.*; 
import java.util.Random; 
import io.github.spl.player.AIPlayer; 
import io.github.spl.player.HumanPlayer; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.ShipTemplate; 

/**
 * TODO description
 */
public abstract   class  GameView {
	
	
	protected ConcurrentLinkedQueue<GameAction> gameActions;

	
	protected Game game;

	
	protected BufferedOutputStream outputStream;

	

	public GameView  () {
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		this.outputStream = new BufferedOutputStream(new ByteArrayOutputStream());
	
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		this.game = new Game(createBasicGameType(), this);
	}

	

	 private void  run__wrappee__Base  () {
		Thread thread = new Thread() {
			public void run() {
				game.play();
			}
		};

		thread.start();

		GameAction action = null;
		while (!(action instanceof GameWin)) {
			if (!gameActions.isEmpty()) {
				action = gameActions.poll();
				processGameAction(action);
			}
		}

	}

	
    public void run() {
        HumanPlayer player1 = new HumanPlayer("Human", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
        //humanPlayer.addShip(game.getGameType().getTemplates().get(0), new Coordinate(1, 1), 0);
        //setupRandomFleet(player1, game.getGameType().getTemplates());

        AIPlayer player2 = new AIPlayer("AI", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
        // aiPlayer.addShip(game.getGameType().getTemplates().get(0), new Coordinate(2, 2), 0);
        // setupRandomFleet(player2, game.getGameType().getTemplates());
		
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

	
	
	public void addGameAction(GameAction action) {
		gameActions.add(action);
	}

	
	
	public ConcurrentLinkedQueue<GameAction> getGameActions() {
		return gameActions;
	}

	

	public BufferedOutputStream getOutputStream() {
		return outputStream;
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

	
 
	public static GameType createBasicGameType() {
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		coordinateList.add(new Coordinate(0, 0));

		ShipTemplate ship1 = new ShipTemplate("basic", coordinateList);

		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();
		shipTemplates.add(ship1);

		//GameType standartType = new GameType(new Dimension(10, 10), shipTemplates);
		GameType standartType = new GameType(new Dimension(10, 10), createBasicFleet());
		return standartType;
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
