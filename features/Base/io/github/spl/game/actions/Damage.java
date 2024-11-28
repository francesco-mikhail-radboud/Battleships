package io.github.spl.game.actions;

import io.github.spl.ships.*;
import io.github.spl.player.*;

/**
 * TODO description
 */
public class Damage implements GameAction {
    private Player attacker;
    private Player defender;
    private Ship ship;
    private ShipCoordinate hitCoordinate;

    public Damage(Player attacker, Player defender, Ship ship, ShipCoordinate hitCoordinate) {
        this.attacker = attacker;
        this.defender = defender;
        this.ship = ship;
        this.hitCoordinate = hitCoordinate;
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

    public ShipCoordinate getHitCoordinate() {
        return hitCoordinate;
    }
}