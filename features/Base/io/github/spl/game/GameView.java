package io.github.spl.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.*;

/**
 * TODO description
 */
public abstract class GameView {
	
	private ConcurrentLinkedQueue<GameAction> gameActions;
	private Game game;

	public GameView() {
		gameActions = new ConcurrentLinkedQueue<GameAction>();
	}

	public void run() {
		Thread thread = new Thread() {
			public void run() {
				game.play();
			}
		};

		thread.start();

		GameAction action = null;
		while (!(action instanceof GameWin)) {
			if (!gameActions.isEmpty()) {
				action = gameActions.poll();
				processGameAction(action);
			}
		}

	}

	private void processGameAction(GameAction action) {
		if (action instanceof RequestCoordinates) {
			processRequestCoordinates((RequestCoordinates) action);
		} else if (action instanceof Hit) {
			processHit((Hit) action);
		} else if (action instanceof Damage) {
			processDamage((Damage) action);
		} else if (action instanceof Miss) {
			processMiss((Miss) action);
		} else if (action instanceof Sinkage) {
			processSinkage((Sinkage) action);
		} else if (action instanceof GameWin) {
			processGameWin((GameWin) action);
		}
	}

	private void processRequestCoordinates(RequestCoordinates action) {

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