package io.github.spl.player;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.spl.game.GameGrid;
import io.github.spl.game.GameView;
import io.github.spl.protocol.Command;
import io.github.spl.protocol.ProtocolParser;
import io.github.spl.protocol.RequestHit;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class Player {
	private GameGrid gameGrid;
	private List<Ship> ships;
	private GameView gameView;
	
	private BufferedReader inputReader;
	private BufferedWriter outputWriter;
	
	public Player(GameGrid gameGrid, List<Ship> ships, GameView gameView) {
		this.gameGrid = gameGrid;
		this.ships = new ArrayList<Ship>(ships);
		this.gameView = gameView;
	}
	
	public void hit(Player other, Coordinate coordinate) throws IOException {
		int x = coordinate.getX();
		int y = coordinate.getY();
		outputWriter.write((new RequestHit(x, y)).toString());
		outputWriter.flush();
	}
	
	public Command read() throws IOException {
		Command command = ProtocolParser.parse(inputReader.readLine());
		return command;
	}
}