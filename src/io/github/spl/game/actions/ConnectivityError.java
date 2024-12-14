package io.github.spl.game.actions; 

import io.github.spl.player.*; 

/**
 * TODO description
 */
public  class  ConnectivityError  implements GameAction {
	
    private Player player;

	

    public ConnectivityError(Player player) {
        this.player = player;
    }

	

    public Player getPlayer() {
        return player;
    }


}
