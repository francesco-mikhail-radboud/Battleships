package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;

import io.github.spl.player.Player;
import io.github.spl.protocol.*;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.game.actions.*;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class Game {
    public void play() {

        ResponseSetup player1Response = player1.setup();
        ResponseSetup player2Response = player2.setup();
        
        if (!(player1Response.isSuccess() && player2Response.isSuccess())) {
        	return;
        }

        while (true) {
            
        	gameView.addGameAction(new GameTick(player1, player2));
        	
        	// Check if player 1 didn't lose the game
            ResponseGameLost responseLost = player1.isGameLost();
            if (responseLost.isLost()) {
            	gameView.addGameAction(new GameWin(player2));
                break;
            }

            List<Coordinate> coordinates = new ArrayList<Coordinate>();
            for (int i = 0; i <= gameType.getTemplates().size(); i++) {
                coordinates.add(player1.selectCoordinate().getCoordinate());
            }
            ResponseCoordinateList responseCoordinateList = new ResponseCoordinateList(getStep(), coordinates);

            // Process each coordinate
            for (Coordinate coordinate : responseCoordinateList.getCoordinateList()) {
                ResponseHit responseHit = player2.hit(coordinate);

                switch (responseHit.getHitOption()) {
                    case ResponseHitOption.HIT:
                        gameView.addGameAction(new Damage(player1, player2, responseHit.getShipName(), coordinate));
                        break;
                    case ResponseHitOption.MISS:
                        gameView.addGameAction(new Miss(player1, player2, coordinate));
                        break;
                    case ResponseHitOption.SINK:
                        gameView.addGameAction(new Sinkage(player1, player2, coordinate, responseHit.getShipName()));
                        break;
                }
            }

            // perform hit from player 1 to player 2
            /*ResponseCoordinate responseCoordinate = player1.selectCoordinate();
            ResponseHit responseHit = player2.hit(responseCoordinate.getCoordinate());

            switch (responseHit.getHitOption()) {
                case ResponseHitOption.HIT:
                	gameView.addGameAction(new Damage(player1, player2, responseHit.getShipName(), responseCoordinate.getCoordinate()));
                    break;
                case ResponseHitOption.MISS:
                	gameView.addGameAction(new Miss(player1, player2, responseCoordinate.getCoordinate()));
                    break;
                case ResponseHitOption.SINK:
                	gameView.addGameAction(new Sinkage(player1, player2, responseCoordinate.getCoordinate(), responseHit.getShipName()));
                    break;
            }
             */
            
            // Check if player 2 lost the game
            responseLost = player2.isGameLost();
            if (responseLost.isLost()) {
            	gameView.addGameAction(new GameWin(player1));
            	break;
            }

            // swap player 1 and player 2
            Player tmp = player1;
            player1 = player2;
            player2 = tmp;
            
            setStep(getStep() + 1);
        }
    }
}