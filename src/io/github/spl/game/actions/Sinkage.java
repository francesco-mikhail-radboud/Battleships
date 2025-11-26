package io.github.spl.game.actions; 

import io.github.spl.player.*; 
import io.github.spl.ships.*; 

/**
 * TODO description
 */
public  class  Sinkage  implements GameAction {
	
    private Player attacker;

	
    private Player defender;

	
    private Coordinate lastCoordinateHit;

	
    private String shipName;

	
    
    public Sinkage(Player attacker, Player defender, Coordinate lastCoordinateHit, String shipName) {
    	this.attacker = attacker;
    	this.defender = defender;
    	this.lastCoordinateHit = lastCoordinateHit;
    	this.shipName = shipName;
    }

	
    
    public Player getAttacker() {
    	return attacker;
    }

	
    
    public Player getDefender() {
		return defender;
	}

	
    
    public Coordinate getLastCoordinateHit() {
		return lastCoordinateHit;
	}

	
    
    public String getShipName() {
    	return shipName;
    }


}
