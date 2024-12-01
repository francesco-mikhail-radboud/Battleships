package io.github.spl.game; 

import io.github.spl.game.actions.RequestCoordinates; 

import io.github.spl.player.Player; 
import io.github.spl.ships.ShipTemplate; 
import io.github.spl.protocol.*; 
import io.github.spl.protocol.ResponseHit.ResponseHitOption; 
import io.github.spl.game.actions.*; 

/**
 * TODO description
 */
public   class  Game {
	
	
	private Player player1;

	
	
	private Player player2;

	
	
	private GameView gameView;

	

    private GameType gameType;

	
	
    public Game(GameType gameType, GameView gameView) {
        this.gameType = gameType;
        this.gameView = gameView;
    }

	
    public void play  () {

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
            // perform hit from player 1 to player 2
            ResponseCoordinate responseCoordinate = player1.selectCoordinate();
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
        }
    }

	
    
    private void performHit() {
    	
    }

	

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

	

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

	
    
    public Player getPlayer1() {
    	return player1;
    }

	
    
    public Player getPlayer2() {
    	return player2;
    }

	

    public GameType getGameType() {
        return gameType;
    }

	

    public ShipTemplate getShipTemplateByName(String name) {
        for (ShipTemplate shipTemplate : gameType.getTemplates()) {
            if (shipTemplate.getName().equals(name)) {
                return shipTemplate;
            }
        }

        return null;
    }


}
