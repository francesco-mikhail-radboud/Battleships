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
    	LocalPlayer player1 = null;
    	if (IS_HUMAN) {
    		player1 = new HumanPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	} else {
    		player1 = new AIPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	}

        AIPlayer player2 = new AIPlayer("AI", new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);

        game.setPlayer1(player1);
        game.setPlayer2(player2);
        
        original();
	}
}