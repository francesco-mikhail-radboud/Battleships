package io.github.spl.game.actions;

import io.github.spl.ships.*;
import io.github.spl.player.*;

/**
 * TODO description
 */
public class Damage implements GameAction {
    private Player attacker;
    private Player defender;
    
    private String shipName;
    private Coordinate hitCoordinate;

    public Damage(Player attacker, Player defender, String shipName, Coordinate hitCoordinate) {
        this.attacker = attacker;
        this.defender = defender;
        this.shipName = shipName;
        this.hitCoordinate = hitCoordinate;
    }

    public Player getAttacker() {
    	return attacker;
    }
    
    public Player getDefender() {
    	return defender;
    }

    public String getShipName() {
        return shipName;
    }

    public Coordinate getHitCoordinate() {
        return hitCoordinate;
    }
}