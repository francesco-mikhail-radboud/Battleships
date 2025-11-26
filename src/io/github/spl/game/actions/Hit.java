package io.github.spl.game.actions; 

import io.github.spl.player.Player; 
import io.github.spl.ships.Coordinate; 

/**
 * TODO description
 */
public  class  Hit  implements GameAction {
	
    private Player player;

	
    private Coordinate attackedCoordinate;

	

    public Hit(Player player, Coordinate attackedCoordinate) {
        this.player = player;
        this.attackedCoordinate = attackedCoordinate;
    }

	

    public Player getPlayer() {
        return player;
    }

	

    public Coordinate getCoordinate() {
        return attackedCoordinate;
    }


}
