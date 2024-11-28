package io.github.spl.game.actions;

import io.github.spl.player.*;

/**
 * TODO description
 */
public class GameWin implements GameAction {
    private Player player;

    public GameWin(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}