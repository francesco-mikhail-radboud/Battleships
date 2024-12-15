package io.github.spl.game;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spl.game.actions.GameAction;
import io.github.spl.ships.*;
import io.github.spl.player.*;
import io.github.spl.player.LocalPlayer.OnlinePlayerHandler;
import io.github.spl.exceptions.GameViewException;
import io.github.spl.game.*;
import java.util.Random;

/**
 * TODO description
 */
public abstract class GameView {
	
	protected int myPort;
	
	protected String opponentHost;
	protected int opponentPort;
	
	public GameView(String[] args) {
		if (args.length < 3) {
			throw new GameViewException("You should provide: \n"
					+ "- your port number, \n"
					+ "- opponent's host, "
					+ "- and opponent's port numeber \n"
					+ "as last 3 arguments.");
		}
		try {
			myPort = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new GameViewException("Unable to parse the user's port number!");
		}
		opponentHost = args[1];
		try {
			opponentPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			throw new GameViewException("Unable to parse the opponent's port number!");
		}
	}
	
    public void run() {
    	LocalPlayer player1 = null;
    	if (IS_HUMAN) {
    		player1 = new HumanPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	} else {
    		player1 = new AIPlayer(USERNAME, new ArrayList<Ship>(), new GameGrid(game.getGameType().getDimension()), this);
    	}
    	
        Socket socket = null;
        boolean isPlayerOne = false;
        while (true) {
        	System.out.println("Waiting for the opponent to join...");
        	try {
                socket = new Socket(opponentHost, opponentPort);
                break;
            } catch (IOException e) {
            	isPlayerOne = true;
            	try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        OnlinePlayer player2 = new OnlinePlayer(socket, this);
        
        Thread onlineThread = null;
        
        try {
	        LocalPlayer.OnlinePlayerHandler onlinePlayerHandler = 
	        		new LocalPlayer.OnlinePlayerHandler(player1, player1.getServerSocket().accept());
	        player1.setOnlinePlayerHandler(onlinePlayerHandler);
	        
	        onlineThread = new Thread(onlinePlayerHandler);
	        
	        onlineThread.start();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
        if (isPlayerOne) {
        	game.setPlayer1(player1);
        	game.setPlayer2(player2);
        } else {
        	game.setPlayer1(player2);
        	game.setPlayer2(player1);
        }
        
        original();
        
        try {
        	socket.close();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}
}