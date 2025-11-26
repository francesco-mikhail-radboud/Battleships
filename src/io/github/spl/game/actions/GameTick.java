package io.github.spl.game.actions; 

import io.github.spl.player.Player; 

public  class  GameTick  implements GameAction {
	
	
	private Player player1;

	
	
	private Player player2;

	
	
	public GameTick(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	
	
	public Player getPlayer1() {
		return player1;
	}

	
	
	public Player getPlayer2() {
		return player2;
	}


}
