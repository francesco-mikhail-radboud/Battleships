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

public abstract  class  GameGridPanel  extends JPanel {
	
	
	private final Font TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 14);

	
	
	public final static int GAME_GRID_SIZE = 800;

	

	protected JButton[][] gridButtons;

	
	protected int gridHeight;

	
	protected int gridWidth;

	
	protected List<ShipCoordinate> shipCoordinates;

	
	
	public GameGridPanel(Dimension dimension) {
		super();
		
		setPreferredSize(new java.awt.Dimension(GAME_GRID_SIZE, GAME_GRID_SIZE));
		
		this.gridHeight = dimension.getHeight();
		this.gridWidth = dimension.getWidth();
		
		setLayout(new GridLayout(gridHeight, gridWidth));
		this.gridButtons = new JButton[gridHeight][gridWidth];
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				JButton button = new JButton();
				button.setBackground(Color.BLUE);
				button.setEnabled(false);
				button.setFont(TEXT_FONT);
				
				gridButtons[row][col] = button;
				add(button);
			}
		}
	}

	
	
	public JButton[][] getGridButtons() {
		return gridButtons;
	}

	
	
	public void removeAllActionListeners() {
		for (int row = 0; row < gridWidth; row++) {
			for (int col = 0; col < gridHeight; col++) {
				JButton button = gridButtons[row][col];
				ActionListener[] listeners = button.getActionListeners();
		    	for (ActionListener listener : listeners) {
		    		button.removeActionListener(listener);
				}
			}
		}
	}

	
	
	public void clearButtonsText() {
		for (int row = 0; row < gridWidth; row++) {
			for (int col = 0; col < gridHeight; col++) {
				JButton button = gridButtons[row][col];
				button.setText("");
			}
		}
		update();
	}

	
	
	public void setButtonText(int row, int col, String text) {
		if (row >= 0 && row < gridHeight && col >= 0 && col < gridWidth) {
			JButton button = gridButtons[row][col];
			button.setText(text);
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

	}


}
