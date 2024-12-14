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
}