package io.github.spl.game;

import io.github.spl.game.actions.RequestCoordinates;
import io.github.spl.player.Player;
import io.github.spl.ships.ShipTemplate;

/**
 * TODO description
 */
public class Game {
	
	private Player player1;
	
	private Player player2;
	
	private GameView gameView;

    private GameType gameType;
	
    public Game(GameType gameType, GameView gameView) {
        this.gameType = gameType;
        this.gameView = gameView;
    }
    
    public void play() {

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