package io.github.spl.game.actions; 

import io.github.spl.player.Player; 

public  class  Setup  implements GameAction {
	
    private Player player;

	
	
	public Setup(Player player) {
		this.player = player;
	}

	
	
	public Player getPlayer() {
		return player;
	}


}
