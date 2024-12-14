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

public  class  GameGridPanel  extends JPanel {
	
	private List<ShipCoordinate> shipCoordinates;

	
	private JButton[][] gridButtons;

	
	private int gridHeight;

	
	private int gridWidth;

	
	private boolean isPlayer;

	
	
	public GameGridPanel(GameGrid gameGrid) {
		super();
		this.shipCoordinates = gameGrid.getListOfCoordsHit();
		this.isPlayer = false;
		
		this.gridHeight = gameGrid.getDimension().getHeight();
		this.gridWidth = gameGrid.getDimension().getWidth();
		
		setLayout(new GridLayout(gridHeight, gridWidth));
		this.gridButtons = new JButton[gridHeight][gridWidth];
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				JButton button = new JButton();
				button.setBackground(Color.BLUE);
				button.setEnabled(false);
				
				gridButtons[row][col] = button;
				add(button);
			}
		}
	}

	
	
	public JButton[][] getGridButtons() {
		return gridButtons;
	}

	
	
	public GameGridPanel(List<Ship> ships, Dimension dimension) {
		super();
		this.shipCoordinates = new ArrayList<ShipCoordinate>();
		for (Ship ship : ships) {
			shipCoordinates.addAll(ship.getShipCoordinates());
		}
		
		this.isPlayer = true;
		
		this.gridHeight = dimension.getHeight();
		this.gridWidth = dimension.getWidth();
		
		setLayout(new GridLayout(gridHeight, gridWidth));
		this.gridButtons = new JButton[gridHeight][gridWidth];
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				JButton button = new JButton();
				button.setBackground(Color.BLUE);
				button.setEnabled(false);
				
				gridButtons[row][col] = button;
				add(button);
			}
		}
	}

	
	
	public void setEnabled(boolean isEnabled) {
		for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
            	gridButtons[row][col].setEnabled(isEnabled);
            }
        }
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
                    	if (isPlayer) {
                    		gridButtons[row][col].setBackground(Color.YELLOW);
                    	} else {
                    		gridButtons[row][col].setBackground(Color.WHITE);
                    		gridButtons[row][col].setText("O");
                    	}
                    }
                } else {
                	gridButtons[row][col].setBackground(Color.BLUE);
                }
			}
		}
	}


}
