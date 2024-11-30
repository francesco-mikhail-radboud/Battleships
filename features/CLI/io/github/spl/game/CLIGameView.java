package io.github.spl.game;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;

import io.github.spl.game.*;
import io.github.spl.game.actions.*;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class CLIGameView extends GameView {
	
	private Scanner scanner;
	
	public CLIGameView() {
		super();

		this.scanner = new Scanner(System.in);
	}

	private void processRequestCoordinates(RequestCoordinates action) throws IOException {

		//TODO: grafical grid
		displayBoardWithShipsCLI(action.getAttacker().getShips(), action.getAttacker().getGameGrid().getDimension());

		System.out.print("Select the coordinate to hit (row column):");
		int x = scanner.nextInt();
		int y = scanner.nextInt();

		//validate the input 
		int gridWidth = action.getDefender().getGameGrid().getDimension().getWidth();
		int gridHeight = action.getDefender().getGameGrid().getDimension().getHeight();
	
		while (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight || 
			   !checkListHits(x, y, action.getDefender().getGameGrid())) {
			if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight) {
				System.out.print("The selected coordinates are incorrect, please try again (row column):");
			} else {
				System.out.print("The selected coordinates have already been affected, select different ones (row column):");
			}
			x = scanner.nextInt();
			y = scanner.nextInt();
		}

		action.getAttacker().hit(action.getDefender(), new Coordinate(x, y));
	}
	
	private void processHit(Hit action) {
		
	}

	private void processDamage(Damage action) {
		
	}

	private void processMiss(Miss action) {
		
	}

	private void processSinkage(Sinkage action) {
		
	}

	private void processGameWin(GameWin action) {
		
	}


	public boolean checkListHits(int x, int y, GameGrid gameGrid) {
		for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
			if (coord.getX() == x && coord.getY() == y) {
				return false;
			}
		}
		return true;
	}


	

}