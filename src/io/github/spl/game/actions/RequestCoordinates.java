package io.github.spl.game.actions; 

import io.github.spl.game.*; 
import io.github.spl.player.*; 

public  class  RequestCoordinates  implements GameAction {
	
    private Player player;

	

    public RequestCoordinates(Player player) {
        this.player = player;
    }

	
    
    public Player getPlayer() {
        return player;
    }


}
