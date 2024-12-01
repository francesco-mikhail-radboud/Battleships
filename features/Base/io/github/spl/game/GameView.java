package io.github.spl.game;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.*;

/**
 * TODO description
 */
public abstract class GameView {
	
	protected ConcurrentLinkedQueue<GameAction> gameActions;
	protected Game game;
	protected BufferedOutputStream outputStream;

	public GameView() {
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		this.outputStream = new BufferedOutputStream(new ByteArrayOutputStream());
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
		} else if (action instanceof GameTick) {
			processGameTick((GameTick) action);
		}
	}

	protected void processRequestCoordinates(RequestCoordinates action) {}
	
	protected void processHit(Hit action) {}

	protected void processDamage(Damage action) {}

	protected void processMiss(Miss action) {}

	protected void processSinkage(Sinkage action) {}

	protected void processGameWin(GameWin action) {}

	protected void processGameTick(GameTick action) {}
	
	public void addGameAction(GameAction action) {
		gameActions.add(action);
	}
	
	public ConcurrentLinkedQueue<GameAction> getGameActions() {
		return gameActions;
	}

	public BufferedOutputStream getOutputStream() {
		return outputStream;
	}
}