package io.github.spl.game.actions;

import io.github.spl.player.Player;
import io.github.spl.ships.Coordinate;

/**
 * TODO description
 */
public class Hit implements GameAction {
    private Player attacker;
    private Player defender;
    private Coordinate attackedCoordinate;

    public Hit(Player attacker, Player defender, Coordinate attackedCoordinate) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackedCoordinate = attackedCoordinate;
    }

    public Player getAttacker() {
        return attacker;
    }
    
    public Player getDefender() {
        return defender;
    }

    public Coordinate getCoordinate() {
        return attackedCoordinate;
    }
}