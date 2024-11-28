package io.github.spl.game.actions;

import io.github.spl.player.Player;
import io.github.spl.ships.Ship;
import io.github.spl.ships.ShipCoordinate;

/**
 * TODO description
 */
public class Sinkage implements GameAction {
    private Player attacker;
    private Player defender;
    private Ship ship;
    
    public Sinkage(Player attacker, Player defender, Ship ship) {
    	this.attacker = attacker;
    	this.defender = defender;
    	this.ship = ship;
    }
    
    public Player getAttacker() {
    	return attacker;
    }
    
    public Player getDefender() {
    	return defender;
    }
    
    public Ship getShip() {
    	return ship;
    }
}