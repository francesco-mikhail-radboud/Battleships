package io.github.spl.game; 

import java.util.ArrayList; 
import java.util.List; 
import io.github.spl.game.actions.*; 

import io.github.spl.ships.ShipTemplate; 

/**
 * TODO description
 */
public  class  GameType {
	
	private Dimension dimension;

	
	private List<ShipTemplate> templates;

	
	
	public GameType(Dimension dimension, List<ShipTemplate> templates) {
		this.dimension = dimension;
		this.templates = new ArrayList<ShipTemplate>(templates);
	}

	

	public Dimension getDimension() {
		return dimension;
	}

	

	public List<ShipTemplate> getTemplates() {
		return templates;
	}


}
