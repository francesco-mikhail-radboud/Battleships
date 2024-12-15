package io.github.spl.game; 

import javax.swing.*; 
import java.awt.*; 
import java.util.ArrayList; 
import java.util.List; 
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 

import io.github.spl.game.actions.*; 
import io.github.spl.player.*; 
import io.github.spl.ships.*; 
import io.github.spl.protocol.*; 

public  class  OpponentGridPanel  extends GameGridPanel {
	
	public OpponentGridPanel(GameGrid gameGrid) {
		super(gameGrid.getDimension());
		this.shipCoordinates = gameGrid.getListOfCoordsHit();
	}

	
	
	public void update() {
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				ShipCoordinate current = new ShipCoordinate(row, col);
				
				ShipCoordinate hitCoord = null;
                for (ShipCoordinate coord : shipCoordinates){
                    if(coord.equals(current)){
                        hitCoord = coord; 
                        break;
                    }
                }
                if (hitCoord != null) {
                    if (hitCoord.getIsHit()) {
                        gridButtons[row][col].setBackground(Color.RED);
                        gridButtons[row][col].setText("X");
                    } else {
                		gridButtons[row][col].setBackground(Color.WHITE);
                		gridButtons[row][col].setText("O");
                    }
                } else {
                	gridButtons[row][col].setBackground(Color.BLUE);
                }
			}
		}
	}


}
