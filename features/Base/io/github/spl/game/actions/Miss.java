package io.github.spl.game.actions;

import io.github.spl.player.*;
import io.github.spl.ships.*;

/**
 * TODO description
 */
public class Miss implements GameAction {
    private Player attacker;
    private Player defender;
    private Coordinate missCoordinate;

    public Miss(Player attacker, Player defender, Coordinate missCoordinate) {
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

    public Coordinate getMissCoordinate() {
        return missCoordinate;
    }
}