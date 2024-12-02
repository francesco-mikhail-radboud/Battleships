package io.github.spl.game; 

import java.util.Scanner; 

import java.util.List; 

import io.github.spl.game.actions.*;  
import io.github.spl.player.*;  
import io.github.spl.ships.*;  
import io.github.spl.protocol.*; 
import java.util.ArrayList; 

/**
 * TODO description
 */
public  class  CLIGameView  extends GameView {
	
	
	private final Scanner scanner;

	
	
	public CLIGameView() {
		super();

		this.scanner = new Scanner(System.in);
	}

	

	protected void processRequestCoordinates(RequestCoordinates action) {

		HumanPlayer humanPlayer = ((HumanPlayer) action.getPlayer());

		System.out.print("Select the coordinate to hit (row column):");
		int x = scanner.nextInt();
		int y = scanner.nextInt();

		//validate the input 
		int gridWidth = game.getGameType().getDimension().getWidth();
		int gridHeight = game.getGameType().getDimension().getHeight();
	
		while (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight || 
			   !checkListHits(x, y, humanPlayer.getGameGrid())) {
			if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight) {
				System.out.print("The selected coordinates are incorrect, please try again (row column):");
			} else {
				System.out.print("The selected coordinates have already been affected, select different ones (row column):");
			}
			x = scanner.nextInt();
			y = scanner.nextInt();
		}

		humanPlayer.getCommandQueue().add(new ResponseCoordinate(x, y));
	}

	
	
	protected void processHit(Hit action) {
		System.out.println("Player " + action.getPlayer().getName() + " is attacked, at the coordinate: " + action.getCoordinate().toString());
	}

	

	protected void processDamage(Damage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getHitCoordinate());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		System.out.println("Player " + action.getDefender().getName() + 
			" is attacked by " + action.getAttacker().getName() + " , Result: ship \"" + action.getShipName() + "\" is damaged in the coordinate: " + action.getHitCoordinate().toString());
	}

	

	protected void processMiss(Miss action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getMissCoordinate());
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		System.out.println("Player " + action.getDefender().getName() + 
			" is attacked by " + action.getAttacker().getName() + ", Result: miss in the coordinate: " + action.getMissCoordinate().toString());
	}

	

	protected void processSinkage(Sinkage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getLastCoordinateHit());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		System.out.println("Player " + action.getDefender().getName() + 
				" is attacked by " + action.getAttacker().getName() + ", Result: ship \"" + action.getShipName() + "\" sank.");
	}

	

	protected void processGameWin(GameWin action) {
		System.out.println("Player " + action.getPlayer().getName() + " won the game!");
	}

	
	
	protected void processGameTick(GameTick action) {
		if (action.getPlayer1() instanceof HumanPlayer) {
			HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer1();
			CLIDisplay.displayYourGrid(humanPlayer.getShips(), humanPlayer.getGameGrid().getDimension());
			CLIDisplay.displayGridHits(humanPlayer.getGameGrid().getListOfCoordsHit(), humanPlayer.getGameGrid().getDimension());	
		} 
	}

	

	protected void processSetup(Setup action) {
		if (action.getPlayer() instanceof HumanPlayer) {
			HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer();
			setupFleetFromUserInput(humanPlayer, game.getGameType().getTemplates());

			humanPlayer.getCommandQueue().add(new ResponseSetup(true));
		} else if (action.getPlayer() instanceof AIPlayer) {
			AIPlayer aiPlayer = (AIPlayer) action.getPlayer();
			setupRandomFleet(aiPlayer, game.getGameType().getTemplates());
			
			aiPlayer.getCommandQueue().add(new ResponseSetup(true));
		}
	}

	

	public boolean checkListHits(int x, int y, GameGrid gameGrid) {
		for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
			if (coord.getX() == x && coord.getY() == y) {
				return false;
			}
		}
		return true;
	}

	

	public void setupFleetFromUserInput(HumanPlayer player, List<ShipTemplate> shipTemplates) {
		GameGrid gameGrid = player.getGameGrid();
		List<Ship> ships = new ArrayList<Ship>();
	
		System.out.println("Welcome to Battleships! Time to place your fleet.");
		for (ShipTemplate template : shipTemplates) {
			boolean placed = false;
	
			while (!placed) {
				System.out.println("Placing ship: " + template.getName() + " (Length: " + template.getCoordinates().size() + ")");
				System.out.print("Enter starting coordinate (row column): ");
				int row = scanner.nextInt();
				int col = scanner.nextInt();
	
				System.out.print("Enter rotation (0=Horizontal, 1=Vertical, 2=180°, 3=270°): ");
				int rotation = scanner.nextInt();
	
				Coordinate startingCoordinate = new Coordinate(row, col);
				Ship ship = new Ship(template, startingCoordinate, rotation);
	
				if (GameView.canPlaceShip(ship, ships, gameGrid)) {
					ships.add(ship);
					player.addShip(template, startingCoordinate, rotation);
					placed = true;
					CLIDisplay.displayYourGrid(player.getShips(), player.getGameGrid().getDimension());
					System.out.println("Ship placed successfully!\n");
				} else {
					System.out.println("Invalid placement. The ship is either out of bounds or overlaps with another ship. Please try again.");
				}
			}
		}
		System.out.println("Fleet placement complete!");
	}


}
