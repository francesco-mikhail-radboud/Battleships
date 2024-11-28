package io.github.spl.game.actions; 

import io.github.spl.game.*; 
import io.github.spl.player.*; 

public  class  RequestCoordinates  implements GameAction {
	
    private Player attacker;

	
    private Player defender;

	

    public RequestCoordinates(Player attacker, Player defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

	
    
    public Player getAttacker() {
        return attacker;
    }

	

    public Player getDefender() {
        return defender;
    }


}
