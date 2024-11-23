package io.github.spl.game;

import java.util.ArrayList;
import java.util.List;

import io.github.spl.ships.ShipTemplate;

/**
 * TODO description
 */
public class GameType {
	private Dimension dimension;
	private List<ShipTemplate> templates;
	
	public GameType(Dimension dimension, List<ShipTemplate> templates) {
		this.dimension = dimension;
		this.templates = new ArrayList<ShipTemplate>(templates);
	}
}