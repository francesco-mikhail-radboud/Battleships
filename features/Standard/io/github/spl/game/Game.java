package io.github.spl.game;

import io.github.spl.player.Player;
import io.github.spl.protocol.*;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.game.actions.*;

/**
 * TODO description
 */
public class Game {
    public void play() {

        ResponseSetup player1Response = player1.setup();
        if (player1Response == null) {
        	gameView.addGameAction(new ConnectivityError(player1));
        	return;
        }
        ResponseSetup player2Response = player2.setup();
        if (player2Response == null) {
        	gameView.addGameAction(new ConnectivityError(player2));
        	return;
        }
        
        if (!(player1Response.isSuccess() && player2Response.isSuccess())) {
        	return;
        }

        while (true) {
            
        	gameView.addGameAction(new GameTick(player1, player2));
        	
        	// Check if player 1 didn't lose the game
            System.out.println("Player1 is game lost:");
            ResponseGameLost responseLost = player1.isGameLost();
            if (responseLost == null) {
            	gameView.addGameAction(new ConnectivityError(player1));
            	break;
            }
            if (responseLost.isLost()) {
            	gameView.addGameAction(new GameWin(player2));
                break;
            }
            // perform hit from player 1 to player 2
            System.out.println("Player1 select coordinate");
            ResponseCoordinate responseCoordinate = player1.selectCoordinate();
            if (responseCoordinate == null) {
            	gameView.addGameAction(new ConnectivityError(player1));
            	break;
            }
            System.out.println("Player2 hit");
            ResponseHit responseHit = player2.hit(responseCoordinate.getCoordinate());
            if (responseHit == null) {
            	gameView.addGameAction(new ConnectivityError(player2));
            	break;
            }
            switch (responseHit.getHitOption()) {
                case HIT:
                	gameView.addGameAction(new Damage(player1, player2, responseHit.getShipName(), responseCoordinate.getCoordinate()));
                    break;
                case MISS:
                	gameView.addGameAction(new Miss(player1, player2, responseCoordinate.getCoordinate()));
                    break;
                case SINK:
                	gameView.addGameAction(new Sinkage(player1, player2, responseCoordinate.getCoordinate(), responseHit.getShipName()));
                    break;
            }
            
            // Check if player 2 lost the game
            System.out.println("Player2 is game lost");
            responseLost = player2.isGameLost();
            if (responseLost == null) {
            	gameView.addGameAction(new ConnectivityError(player2));
            	break;
            }
            if (responseLost.isLost()) {
            	gameView.addGameAction(new GameWin(player1));
            	break;
            }

            // swap player 1 and player 2
            System.out.println("Swap");
            Player tmp = player1;
            player1 = player2;
            player2 = tmp;
            
            setStep(getStep() + 1);
        }
    }
}