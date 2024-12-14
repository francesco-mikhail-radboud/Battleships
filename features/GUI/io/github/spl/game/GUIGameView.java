package io.github.spl.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import io.github.spl.game.actions.*;
import io.github.spl.player.*;
import io.github.spl.ships.*;
import io.github.spl.protocol.*;

public class GUIGameView extends GameView {
	
	private JFrame gameFrame;
	
	private GameGridPanel player1GameGridPanel;
	private GameGridPanel player2GameGridPanel;	

    public GUIGameView(String[] args) {
        super(args);
    }
    
    public void run() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
		    	gameFrame = new JFrame();
		    	gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				gameFrame.setPreferredSize(new Dimension(1200, 800));
		    	
		    	gameFrame.pack();
		    	gameFrame.setLocationRelativeTo(null);
		    	gameFrame.setVisible(true);
			}
		});

    	super.run();
    }

    protected void processRequestCoordinates(RequestCoordinates action) {
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
								humanPlayer.getCommandQueue().add(new ResponseCoordinate(game.getStep(), finalRow, finalCol));
								player2GameGridPanel.setEnabled(false);
								for (int row = 0; row < gridWidth; row++) {
									for (int col = 0; col < gridHeight; col++) {
										removeAllActionListeners(player2GameGridPanel.getGridButtons()[row][col]);
									}
								}
							}
						});
		    		}
		    	}
			}
		});
    }
    
    private void removeAllActionListeners(JButton button) {
    	ActionListener[] listeners = button.getActionListeners();
    	for (ActionListener listener : listeners) {
    		button.removeActionListener(listener);
		}
    }
	
	protected void processHit(Hit action) {
		System.out.println("Hello");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					JOptionPane.showMessageDialog(gameFrame, 
							"Player " + action.getPlayer().getName() + 
							" is attacked, at the coordinate: " + 
							action.getCoordinate().toString());
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}

	protected void processDamage(Damage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getHitCoordinate());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		System.out.println("Hello");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					JOptionPane.showMessageDialog(gameFrame, 
							"Player " + action.getDefender().getName() + 
							" is attacked by " + action.getAttacker().getName() + 
							" , Result: ship \"" + action.getShipName() + 
							"\" is damaged in the coordinate: " + 
							action.getHitCoordinate().toString());	
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}

	protected void processMiss(Miss action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getMissCoordinate());
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		System.out.println("Hello");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					JOptionPane.showMessageDialog(gameFrame, 
							"Player " + action.getDefender().getName() + 
							" is attacked by " + action.getAttacker().getName() + 
							", Result: miss in the coordinate: " + 
							action.getMissCoordinate().toString());	
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}

	protected void processSinkage(Sinkage action) {
		if (action.getAttacker() instanceof LocalPlayer) {
			ShipCoordinate coordinate = new ShipCoordinate(action.getLastCoordinateHit());
			coordinate.setIsHit(true);
			((LocalPlayer) action.getAttacker()).getGameGrid().add(coordinate);
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					JOptionPane.showMessageDialog(gameFrame, 
							"Player " + action.getDefender().getName() + 
							" is attacked by " + action.getAttacker().getName() + 
							", Result: ship \"" + action.getShipName() + "\" sank.");
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}

	protected void processGameWin(GameWin action) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					JOptionPane.showMessageDialog(gameFrame, 
							"Player " + action.getPlayer().getName() + " won the game!");
					gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
				}
			});
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
	        throw new RuntimeException(e);
	    }
	}

	protected void processGameTick(GameTick action) {
		if (action.getPlayer1() instanceof HumanPlayer) {
			SwingUtilities.invokeLater(new Runnable() {	
				@Override
				public void run() {
					player1GameGridPanel.update();
					player2GameGridPanel.update();
					gameFrame.revalidate();
				}
			});
		}
	}

	protected void processSetup(Setup action) {
		if (action.getPlayer() instanceof HumanPlayer) {
			HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer();
			//setupFleetFromUserInput(humanPlayer, game.getGameType().getTemplates());
			setupRandomFleet(humanPlayer, game.getGameType().getTemplates());
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					player1GameGridPanel = new GameGridPanel(humanPlayer.getShips(), game.getGameType().getDimension());
					player1GameGridPanel.update();
					
					player2GameGridPanel = new GameGridPanel(humanPlayer.getGameGrid());
					player2GameGridPanel.update();	
					
					gameFrame.getContentPane().removeAll();
					gameFrame.repaint();
					
					gameFrame.setLayout(new GridLayout());
					
					gameFrame.add(player1GameGridPanel);
					gameFrame.add(player2GameGridPanel);
					
					gameFrame.revalidate();
				}
			});
			
			humanPlayer.getCommandQueue().add(new ResponseSetup(game.getStep(), true));
		} else if (action.getPlayer() instanceof AIPlayer) {
			AIPlayer aiPlayer = (AIPlayer) action.getPlayer();
			setupRandomFleet(aiPlayer, game.getGameType().getTemplates());
			
			aiPlayer.getCommandQueue().add(new ResponseSetup(game.getStep(), true)); 
		}
	}
	
	protected void processConnectivityError(ConnectivityError action) {
		
	}
}
