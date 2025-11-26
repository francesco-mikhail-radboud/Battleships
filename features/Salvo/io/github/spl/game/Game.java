package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;

import io.github.spl.player.Player;
import io.github.spl.protocol.*;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.game.actions.*;
import io.github.spl.ships.*;

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
            ResponseGameLost responseLost = player1.isGameLost();
            if (responseLost == null) {
            	gameView.addGameAction(new ConnectivityError(player1));
            	break;
            }
            if (responseLost.isLost()) {
            	gameView.addGameAction(new GameWin(player2));
                break;
            }

            List<Coordinate> coordinates = new ArrayList<Coordinate>();
            for (int i = 0; i < gameType.getTemplates().size(); i++) {
            	int oldStep = getStep();
            	setStep(oldStep * gameType.getTemplates().size() + i);
            	ResponseCoordinate responseCoordinate = player1.selectCoordinate();
            	setStep(oldStep);
                if (responseCoordinate == null) {
                	gameView.addGameAction(new ConnectivityError(player1));
                	return;
                }
                coordinates.add(responseCoordinate.getCoordinate());
            }
            ResponseCoordinateList responseCoordinateList = new ResponseCoordinateList(getStep(), coordinates);

            // Process each coordinate
            for (int i = 0; i < responseCoordinateList.getCoordinateList().size(); i++) {
            	int oldStep = getStep();
            	setStep(oldStep * gameType.getTemplates().size() + i);
            	Coordinate coordinate = responseCoordinateList.getCoordinateList().get(i);
            	ResponseHit responseHit = player2.hit(coordinate);
            	setStep(oldStep);
                if (responseHit == null) {
                	gameView.addGameAction(new ConnectivityError(player2));
                	return;
                }
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

            // Check if player 2 lost the game
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
            Player tmp = player1;
            player1 = player2;
            player2 = tmp;
            
            setStep(getStep() + 1);
        }
    }
}