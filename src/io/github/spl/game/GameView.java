package io.github.spl.game; 
import java.util.concurrent.ConcurrentLinkedQueue; 

import io.github.spl.game.actions.*; 

import java.util.ArrayList; 
import java.util.List; 

import io.github.spl.game.actions.GameAction; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.ShipTemplate; 

/**
 * TODO description
 */
public abstract   class  GameView {
	
	
	private ConcurrentLinkedQueue<GameAction> gameActions;

	
	private Game game;

	

	public GameView  () {
		gameActions = new ConcurrentLinkedQueue<GameAction>();
	
		this.gameActions = new ConcurrentLinkedQueue<GameAction>();
		this.game = new Game(createBasicGameType(), this);
	}

	

	public void run  () {
		
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

	

	public static GameType createBasicGameType() {
		List<Coordinate> coordinateList = new ArrayList<Coordinate>();
		coordinateList.add(new Coordinate(0, 0));

		ShipTemplate ship1 = new ShipTemplate("basic", coordinateList);

		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();
		shipTemplates.add(ship1);

		GameType standartType = new GameType(new Dimension(10, 10), shipTemplates);
		return standartType;
	}


}
