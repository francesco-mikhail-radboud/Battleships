package io.github.spl.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.spl.game.actions.*;
import io.github.spl.player.*;
import io.github.spl.ships.*;
import io.github.spl.protocol.*;

public class GUIGameView extends GameView {
	
	private JFrame gameFrame;
	
	private GameGridPanel player1GameGridPanel;
	private GameGridPanel player2GameGridPanel;
	
	private final int AI_PLAYER_DELAY = 100;
	private final Font TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
	
	private final int BORDER_SIZE = 25;
	
	private JTextArea messageArea;

	private boolean GAME_FRAME_IS_READY = false;
	private boolean PLAYER_TURN = false;
	
    public GUIGameView(String[] args) {
        super(args);
    }
    
    public void run() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
		    	gameFrame = new JFrame();
		    	gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				
				JPanel panel = new JPanel();
				JLabel label = new JLabel("Waiting for the game to start...");
				label.setFont(TEXT_FONT);
				panel.add(label);
				
				setupGameFrame(panel, panel);
		    	
		    	gameFrame.pack();
		    	gameFrame.setLocationRelativeTo(null);
		    	gameFrame.setVisible(true);
			}
		});

    	super.run();
    	
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
    }
    
    private void setupGameFrame(JPanel panel1, JPanel panel2) {
    	gameFrame.getContentPane().removeAll();
		gameFrame.repaint();
		
		gameFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		gameFrame.setResizable(false);
    	
    	JPanel gridsContainer = new JPanel();
    	gridsContainer.setLayout(new BoxLayout(gridsContainer, BoxLayout.X_AXIS));
   
    	panel1.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    	panel1.setPreferredSize(new Dimension(GameGridPanel.GAME_GRID_SIZE, GameGridPanel.GAME_GRID_SIZE));
    	
    	panel2.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    	panel2.setPreferredSize(new Dimension(GameGridPanel.GAME_GRID_SIZE, GameGridPanel.GAME_GRID_SIZE));
    	
    	gridsContainer.add(panel1);
    	gridsContainer.add(panel2);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		gameFrame.add(gridsContainer, c);
    	
    	messageArea = new JTextArea(8, 50);
        messageArea.setEditable(false);
        messageArea.setFont(TEXT_FONT);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        
        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
        gameFrame.add(scrollPane, c);
        
        gameFrame.pack();
        gameFrame.revalidate();
    }
    
    private void updateGrids() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					player1GameGridPanel.update();
					player2GameGridPanel.update();
					gameFrame.revalidate();
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}
    
    private void showMessageDialog(String message, JFrame frame) {
    	JLabel label = new JLabel(message);
		label.setFont(TEXT_FONT);
		
		JOptionPane.showMessageDialog(frame, label, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
    
    private void showMessage(String message) {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					showMessageDialog(message, gameFrame);
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
    }
    
    private void printMessage(String message) {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					messageArea.append(message + "\n");
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
    }
    
    private void setFrameTitle(String title) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {	
                @Override
                public void run() {
                    gameFrame.setTitle(title);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void processRequestCoordinates(RequestCoordinates action) {	
    	if (action.getPlayer() instanceof HumanPlayer) {
	    	HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer();
	    	
	    	int gridWidth = game.getGameType().getDimension().getWidth();
			int gridHeight = game.getGameType().getDimension().getHeight();
	    	
			SwingUtilities.invokeLater(new Runnable() {	
				@Override
				public void run() {
					player2GameGridPanel.setEnabled(true);
			    	for (int row = 0; row < gridWidth; row++) {
			    		for (int col = 0; col < gridHeight; col++) {
			    			int finalRow = row;
			    			int finalCol = col;
			    			player2GameGridPanel.getGridButtons()[finalRow][finalCol].addActionListener(new ActionListener() {					
								@Override
								public void actionPerformed(ActionEvent e) {
									if (LocalPlayer.checkListHits(finalRow, finalCol, humanPlayer.getGameGrid())) {
										player2GameGridPanel.getGridButtons()[finalRow][finalCol].setBackground(Color.GREEN);
										humanPlayer.getCommandQueue().add(new ResponseCoordinate(game.getStep(), finalRow, finalCol));
										player2GameGridPanel.setEnabled(false);
										player2GameGridPanel.removeAllActionListeners();
									} else {
										showMessageDialog("The selected coordinates have already been affected, select different ones.", gameFrame);
									}
								}
							});
			    		}
			    	}
				}
			});
    	} else if (action.getPlayer() instanceof AIPlayer) {
    		sleep(AI_PLAYER_DELAY);
    		AIPlayer aiPlayer = (AIPlayer) action.getPlayer();
    		aiPlayer.getCommandQueue().add(new ResponseCoordinate(game.getStep(), -1, -1));
    	}
    }
    

	
	protected void processHit(Hit action) {
		String message = "Player " + action.getPlayer().getName() + 
							" is attacked, at the coordinate: " + 
							action.getCoordinate().toString();
		if (action.getPlayer() instanceof HumanPlayer) {	
			showMessage(message);			
		} else if (action.getPlayer() instanceof AIPlayer) {
			sleep(AI_PLAYER_DELAY);
		}
		printMessage(message);
	}

	protected void processDamage(Damage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getHitCoordinate(), action.getShipName());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		String message = "Player " + action.getDefender().getName() + 
				" is attacked by " + action.getAttacker().getName() + 
				" , Result: ship \"" + action.getShipName() + 
				"\" is damaged in the coordinate: " + 
				action.getHitCoordinate().toString();
		if (action.getAttacker() instanceof HumanPlayer) {
			showMessage(message);	
		} else if (action.getAttacker() instanceof AIPlayer) {
			sleep(AI_PLAYER_DELAY);
			((AIPlayer) action.getAttacker()).addAdjacentCoordinates(action.getHitCoordinate(), action.getShipName());
		}
		printMessage(message);
		updateGrids();
	}

	protected void processMiss(Miss action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getMissCoordinate());
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		String message = "Player " + action.getDefender().getName() + 
				" is attacked by " + action.getAttacker().getName() + 
				", Result: miss in the coordinate: " + 
				action.getMissCoordinate().toString();		
		if (action.getAttacker() instanceof HumanPlayer) {
			showMessage(message);	
		} else if (action.getAttacker() instanceof AIPlayer) {
			sleep(AI_PLAYER_DELAY);
		}
		printMessage(message);
		updateGrids();
	}

	protected void processSinkage(Sinkage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getLastCoordinateHit(), action.getShipName());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		String message = "Player " + action.getDefender().getName() + 
				" is attacked by " + action.getAttacker().getName() + 
				", Result: ship \"" + action.getShipName() + "\" sank.";
		if (action.getAttacker() instanceof HumanPlayer) {
			showMessage(message);
		} else if (action.getAttacker() instanceof AIPlayer) {
			sleep(AI_PLAYER_DELAY);
			((AIPlayer) action.getAttacker()).addAdjacentCoordinates(action.getLastCoordinateHit(), action.getShipName());
		}
		printMessage(message);
		updateGrids();
	}

	protected void processGameWin(GameWin action) {
		String message = "Player " + action.getPlayer().getName() + " won the game!";
		showMessage(message);
		printMessage(message);
	}

	protected void processGameTick(GameTick action) {
		if (PLAYER_TURN) {
			setFrameTitle("Make your move.");
		} else {
    		setFrameTitle("Wait for your opponent to make a move.");
		}
		PLAYER_TURN = !PLAYER_TURN;
		updateGrids();
	}

	protected void processSetup(Setup action) {
		if (action.getPlayer() instanceof LocalPlayer) {
			LocalPlayer localPlayer = (LocalPlayer) action.getPlayer();
			
			if (action.getPlayer() instanceof HumanPlayer) {
				HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer();
				
				try {
                    setupFleetFromUserInput(humanPlayer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
				
			} else if (action.getPlayer() instanceof AIPlayer) {
				AIPlayer aiPlayer = (AIPlayer) action.getPlayer();
				setupRandomFleet(aiPlayer, game.getGameType().getTemplates());
				
				aiPlayer.getCommandQueue().add(new ResponseSetup(game.getStep(), true)); 
			}
			
			try {
				if (!GAME_FRAME_IS_READY) {
					PLAYER_TURN = true;
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							player1GameGridPanel = new PlayerGridPanel(localPlayer);
							player1GameGridPanel.update();
							
							player2GameGridPanel = new OpponentGridPanel(localPlayer.getGameGrid());
							player2GameGridPanel.update();	
							
							setupGameFrame(player1GameGridPanel, player2GameGridPanel);
							
							GAME_FRAME_IS_READY = true;
						}
					});
				}
			} catch (InterruptedException e) {
	            throw new RuntimeException(e);
	        } catch (InvocationTargetException e) {
	            throw new RuntimeException(e);
	        }
		}
	}
	
	private JButton createButtonFromShipTemplate(ShipTemplate template) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<html>" + template.getName() + ": <br>");
		int maxX = 0;
		int maxY = 0;
		for (Coordinate coordinate : template.getCoordinates()) {
			if (maxX < coordinate.getX()) {
				maxX = coordinate.getX();
			}
			if (maxY < coordinate.getY()) {
				maxY = coordinate.getY();
			}
		}
		for (int row = 0; row <= maxX; row++) {
			for (int col = 0; col <= maxY; col++) {
				Coordinate coordinate = new Coordinate(row, col);
				if (template.getCoordinates().contains(coordinate)) {
					stringBuilder.append("*");
				} else {
					stringBuilder.append(" ");
				}
			}
			stringBuilder.append("<br>");
		}
		
		JButton button = new JButton();
		button.setFont(TEXT_FONT);
		button.setText(stringBuilder.toString());
		
		stringBuilder.append("</html>");
		
		return button;
	}
	
	private void removeActionListeners(JButton button) {
		ActionListener[] listeners = button.getActionListeners();
    	for (ActionListener listener : listeners) {
    		button.removeActionListener(listener);
		}
	}
	
	private void addActionListenerToPlaceButton(JButton placeButton, JFrame setupFrame, JPanel shipSelectionPanel, 
												GameGridPanel gridPanel, JButton templateButton, List<Ship> ships, HumanPlayer player, 
												ShipTemplate template, Coordinate startingCoordinate, int rotation, AtomicInteger shipsToPlace) {
		Ship ship = new Ship(template, startingCoordinate, rotation);
		placeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GameView.canPlaceShip(ship, ships, player.getGameGrid())) {
					ships.add(ship);
					player.addShip(template, startingCoordinate, rotation);
					
					shipSelectionPanel.remove(templateButton);
					shipsToPlace.set(shipsToPlace.get() - 1);
					
					removeActionListeners(placeButton);
					removeActionListeners(templateButton);
					
					if (shipsToPlace.get() == 0) {
						placeButton.setText("Done");
						placeButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								player1GameGridPanel.update();
								player2GameGridPanel.update();
								gameFrame.revalidate();
								
								setupFrame.setVisible(false);
								gameFrame.setEnabled(true);
								player.getCommandQueue().add(new ResponseSetup(game.getStep(), true));
							}
						});
					}
						
					gridPanel.removeAllActionListeners();
					gridPanel.clearButtonsText();
					gridPanel.setEnabled(false);
					
					gridPanel.update();
					gridPanel.revalidate();
					shipSelectionPanel.revalidate();
					shipSelectionPanel.repaint();
				} else {
					showMessageDialog("Invalid placement. "
							+ "The ship is either out of bounds or overlaps with another ship. "
							+ "Please try again.", setupFrame);
				}
			}
		});
	}
	
	private void setupFleetFromUserInput(HumanPlayer player) throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				gameFrame.setEnabled(false);
				
				JFrame setupFrame = new JFrame("Setup your fleet");
				setupFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				setupFrame.setResizable(false);
				setupFrame.setLayout(new BorderLayout());
				
				GameGridPanel gameGridPanel = new PlayerGridPanel(player);
				gameGridPanel.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
				gameGridPanel.setPreferredSize(new Dimension(GameGridPanel.GAME_GRID_SIZE, GameGridPanel.GAME_GRID_SIZE));
				
				gameGridPanel.update();
				
				setupFrame.add(gameGridPanel, BorderLayout.CENTER);
				
				JPanel shipSelectionPanel = new JPanel();
				shipSelectionPanel.setPreferredSize(new Dimension(GameGridPanel.GAME_GRID_SIZE / 3, GameGridPanel.GAME_GRID_SIZE));
				shipSelectionPanel.setLayout(new BoxLayout(shipSelectionPanel, BoxLayout.Y_AXIS));
				
				setupFrame.add(shipSelectionPanel, BorderLayout.EAST);
				
				JButton placeShipButton = new JButton("Place ship");
				placeShipButton.setFont(TEXT_FONT);
				setupFrame.add(placeShipButton, BorderLayout.SOUTH);
				
				List<Ship> ships = new ArrayList<Ship>();
				
				AtomicInteger shipsToPlace = new AtomicInteger(game.getGameType().getTemplates().size());
				
				for (ShipTemplate template : game.getGameType().getTemplates()) {
					JButton button = createButtonFromShipTemplate(template);
					shipSelectionPanel.add(button);
					button.addActionListener(new ActionListener() {	
						@Override
						public void actionPerformed(ActionEvent e) {	
							gameGridPanel.setEnabled(true);
							gameGridPanel.removeAllActionListeners();
							int gridWidth = game.getGameType().getDimension().getWidth();
							int gridHeight = game.getGameType().getDimension().getHeight();
					    	for (int row = 0; row < gridWidth; row++) {
					    		for (int col = 0; col < gridHeight; col++) {
					    			Coordinate startingCoordinate = new Coordinate(row, col);
									
					    			int finalRow = row;
					    			int finalCol = col;
					    			JButton gridButton = gameGridPanel.getGridButtons()[finalRow][finalCol];
					    			gridButton.addActionListener(new ActionListener() {					
										@Override
										public void actionPerformed(ActionEvent e) {
											if (gridButton.getText().equals("")) {
												gameGridPanel.clearButtonsText();
												removeActionListeners(placeShipButton);
												gridButton.setText(">");
												gridButton.setBackground(Color.WHITE);
												addActionListenerToPlaceButton(placeShipButton, setupFrame, shipSelectionPanel, 
														gameGridPanel, button, ships, player, template, startingCoordinate, 0, shipsToPlace);
											} else if (gridButton.getText().equals(">")) {
												gameGridPanel.clearButtonsText();
												removeActionListeners(placeShipButton);
												gridButton.setText("^");
												gridButton.setBackground(Color.WHITE);
												addActionListenerToPlaceButton(placeShipButton, setupFrame, shipSelectionPanel, 
														gameGridPanel, button, ships, player, template, startingCoordinate, 1, shipsToPlace);
											} else if (gridButton.getText().equals("^")) {
												gameGridPanel.clearButtonsText();
												removeActionListeners(placeShipButton);
												gridButton.setText("<");
												gridButton.setBackground(Color.WHITE);
												addActionListenerToPlaceButton(placeShipButton, setupFrame, shipSelectionPanel, 
														gameGridPanel, button, ships, player, template, startingCoordinate, 2, shipsToPlace);
											} else if (gridButton.getText().equals("<")) {
												gameGridPanel.clearButtonsText();
												removeActionListeners(placeShipButton);
												gridButton.setText("v");
												gridButton.setBackground(Color.WHITE);
												addActionListenerToPlaceButton(placeShipButton, setupFrame, shipSelectionPanel, 
														gameGridPanel, button, ships, player, template, startingCoordinate, 3, shipsToPlace);
											} else if (gridButton.getText().equals("v")) {
												gridButton.setText("");
											}
										}
									});
					    		}
					    	}
						}
					});
				}
				
				setupFrame.pack();
				setupFrame.setVisible(true);
			}
		});
	}
	
	protected void processConnectivityError(ConnectivityError action) {
		String message = "Connection interrupted with the player: " + action.getPlayer().getName();
		showMessage(message);
		printMessage(message);
	}
}
