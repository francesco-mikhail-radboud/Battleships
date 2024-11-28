package io.github.spl.game; 

import java.io.IOException; 
import java.util.Scanner; 
import java.util.concurrent.ConcurrentLinkedQueue; 

import io.github.spl.game.*; 
import io.github.spl.game.actions.*; 
import io.github.spl.ships.*; 

/**
 * TODO description
 */
public  class  CLIGameView  extends GameView {
	

	private Scanner scanner;

	
	
	public CLIGameView() {
		super();

		this.scanner = new Scanner(System.in);
	}

	

	private void processRequestCoordinates(RequestCoordinates action) throws IOException {
		int x = scanner.nextInt();
		int y = scanner.nextInt();

		// TODO: validate the input 

		action.getAttacker().hit(action.getDefender(), new Coordinate(x, y));
	}

	
	
	private void processHit(Hit action) {
		
	}

	

	private void processDamage(Damage action) {
		
	}

	

	private void processMiss(Miss action) {
		
	}

	

	private void processSinkage(Sinkage action) {
		
	}

	

	private void processGameWin(GameWin action) {
		
	}


}
