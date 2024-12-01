package io.github.spl.game;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.List;

import io.github.spl.game.*;
import io.github.spl.game.actions.*;
import io.github.spl.player.*;
import io.github.spl.ships.*;
import io.github.spl.protocol.*;

/**
 * TODO description
 */
public class CLIGameView extends GameView {
	
	private Scanner scanner;
	
	public CLIGameView() {
		super();

		this.scanner = new Scanner(System.in);
	}

	protected void processRequestCoordinates(RequestCoordinates action) {

		HumanPlayer humanPlayer = ((HumanPlayer) action.getPlayer());

		//TODO: grafical grid
		//CLIDisplay.displayGridWithHitsCLI(humanPlayer.getShips(), humanPlayer.getGameGrid().getListOfCoordsHit(), 
		//									humanPlayer.getGameGrid().getDimension(), true);

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


	public boolean checkListHits(int x, int y, GameGrid gameGrid) {
		for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
			if (coord.getX() == x && coord.getY() == y) {
				return false;
			}
		}
		return true;
	}


	

}