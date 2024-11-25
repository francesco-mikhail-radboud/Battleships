package io.github.spl.player;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.spl.game.GameGrid;
import io.github.spl.game.GameView;
import io.github.spl.protocol.Command;
import io.github.spl.protocol.ProtocolParser;
import io.github.spl.protocol.RequestHit;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class AIPlayer implements Player{
	private GameGrid gameGrid;
	private List<Ship> ships;
	private GameView gameView;
	
	private Scanner scanner = new Scanner(System.in);
	
	private BufferedReader inputReader;
	private BufferedWriter outputWriter;
	
	public LocalPlayer(GameGrid gameGrid, List<Ship> ships, GameView gameView) {
		this.gameGrid = gameGrid;
		this.ships = new ArrayList<Ship>(ships);
		this.gameView = gameView;
	}
	
	
	public void hit(Player other) {
		Coordinate xy;
		do {
			xy = coordToHit();
		} while (checkListHits(xy.getX(), xy.getY()))
		
		outputWriter.write(new RequestHit(xy.getX(), xy.getY()));
		outputWriter.flush();
	}
	
	public Command read() {
		Command command = ProtocolParser.parse(inputReader.readLine());
		return command;
	}
	
	
	public boolean checkListHits(int x, int y) {
		for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
			if (coord.getX() == x && coord.getY() == y) {
				return false;
			}
		}
		return true;
	}
	
	public Coordinate coordToHit() {
		System.out.println("Select the coordinate to hit (x y):");
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        return new Coordinate(x, y);
	}
	
	
	
	
}