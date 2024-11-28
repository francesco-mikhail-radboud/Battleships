package io.github.spl.game.actions;

import io.github.spl.player.*;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class Miss implements GameAction {
    private Player attacker;
    private Player defender;
    private ShipCoordinate missCoordinate;

    public Miss(Player attacker, Player defender, ShipCoordinate missCoordinate) {
        this.attacker = attacker;
        this.defender = defender;
        this.missCoordinate = missCoordinate;
    }

     public Player getAttacker() {
        return attacker;
    }
    
    public Player getDefender() {
        return defender;
    }

    public ShipCoordinate getMissCoordinate() {
        return missCoordinate;
    }
}