package io.github.spl.player;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import io.github.spl.game.GameGrid;
import io.github.spl.game.GameView;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.Ship;
import io.github.spl.ships.ShipTemplate;
import io.github.spl.protocol.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.game.actions.*;

public abstract class LocalPlayer implements Player {

    protected String name;
    protected List<Ship> ships;
    protected GameGrid gameGrid;

    protected ConcurrentLinkedQueue<Command> commandQueue;
    protected GameView gameView;
    
    protected ServerSocket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    
    protected ConcurrentLinkedQueue<ResponseHit> LAST_RESPONSE_HIT;
    protected ConcurrentLinkedQueue<ResponseGameLost> LAST_RESPONSE_GAME_LOST;
    protected ConcurrentLinkedQueue<ResponseCoordinate> LAST_RESPONSE_COORDINATE;
    protected ConcurrentLinkedQueue<ResponseSetup> LAST_RESPONSE_SETUP;
    
    protected OnlinePlayerHandler onlinePlayerHandler;
    
    public LocalPlayer(String name, List<Ship> ships, GameGrid gameGrid, GameView gameView, int port) {
        this.name = name;
        this.ships = new ArrayList<Ship>(ships);;
        this.gameGrid = gameGrid;
        this.commandQueue = new ConcurrentLinkedQueue<Command>();
        this.gameView = gameView;
        try {
			this.socket = new ServerSocket(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        this.LAST_RESPONSE_HIT = new ConcurrentLinkedQueue<ResponseHit>();
        this.LAST_RESPONSE_GAME_LOST = new ConcurrentLinkedQueue<ResponseGameLost>();
        this.LAST_RESPONSE_COORDINATE = new ConcurrentLinkedQueue<ResponseCoordinate>();
        this.LAST_RESPONSE_SETUP = new ConcurrentLinkedQueue<ResponseSetup>();
    }

    public boolean addShip(ShipTemplate shipTemplate, Coordinate coordinate, int timesRotated) {
        Ship ship = new Ship(shipTemplate, coordinate, timesRotated);
        ships.add(ship);
        return true;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }

    public ResponseHit hit(Coordinate coordinate) {
        for (Ship ship : ships) {
            if (ship.hit(coordinate)) {
            	ResponseHit responseHit = null;
                if (ship.isSunk()) {
                	responseHit = new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.SINK, ship.getName()); 
                } else {
                	responseHit = new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.HIT, ship.getName());
                }
                LAST_RESPONSE_HIT.add(responseHit);
                return responseHit;
            }
        }
        
        ResponseHit responseHit = new ResponseHit(gameView.getGame().getStep(), ResponseHitOption.MISS, null);
        LAST_RESPONSE_HIT.add(responseHit);

        return responseHit;
    }

    public ResponseGameLost isGameLost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
            	ResponseGameLost responseGameLost = new ResponseGameLost(gameView.getGame().getStep(), false);
            	LAST_RESPONSE_GAME_LOST.add(responseGameLost);
                return responseGameLost;
            }
        }
        
        ResponseGameLost responseGameLost = new ResponseGameLost(gameView.getGame().getStep(), true);
        LAST_RESPONSE_GAME_LOST.add(responseGameLost);

        return responseGameLost;
    }

	public ResponseCoordinate selectCoordinate() {
		return null;
	}

	public String getName() {
        return name;
    }

    public ResponseSetup setup() {
        gameView.addGameAction(new Setup(this));

        Command command = null;
		while (!(command instanceof ResponseSetup)) {
			if (!commandQueue.isEmpty()) {
				command = commandQueue.poll();
			}
		}
		
		ResponseSetup responseSetup = (ResponseSetup) command;
		LAST_RESPONSE_SETUP.add(responseSetup);

        return responseSetup;
    }

    public ConcurrentLinkedQueue<Command> getCommandQueue() {
    	return commandQueue;
    }
    
    public OnlinePlayerHandler getOnlinePlayerHandler() {
    	return onlinePlayerHandler;
    }
    
    public void setOnlinePlayerHandler(OnlinePlayerHandler onlinePlayerHandler) {
    	this.onlinePlayerHandler = onlinePlayerHandler;
    }
    
    public ServerSocket getServerSocket() {
    	return socket;
    }
   
    public static class OnlinePlayerHandler implements Runnable {
    	private final Socket onlinePlayerSocket;
    	private final LocalPlayer localPlayer;
    	private PrintWriter out;
        private BufferedReader in;
        
        public OnlinePlayerHandler(LocalPlayer localPlayer, Socket onlinePlayerSocket) {
        	this.onlinePlayerSocket = onlinePlayerSocket;
        	this.localPlayer = localPlayer;
        	try {
                // Create input and output streams for communication
                out = new PrintWriter(onlinePlayerSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(onlinePlayerSocket.getInputStream()));
            } catch (IOException e) {
                // ignored
            }
        }
        
        @Override
        public void run() {
        	try {
        		String inputLine;
                while ((inputLine = in.readLine()) != null) {
                	Command command = ProtocolParser.parse(inputLine);
                	if (command instanceof ResponseCoordinate) {
                		processRequestHit((ResponseCoordinate) command);
                	} else if (command instanceof RequestGameLost) {
                		processRequestGameLost((RequestGameLost) command);
                	} else if (command instanceof RequestCoordinate) {
                		processRequestCoordinate((RequestCoordinate) command);
                	} else if (command instanceof RequestName) {
                		processRequestName((RequestName) command);
                	} else if (command instanceof RequestSetup) {
                		processRequestSetup((RequestSetup) command);
                	} else if (command instanceof AckCoordinate) {
                		processAckCoordinate((AckCoordinate) command);
                	} else if (command instanceof AckGameLost) {
                		processAckGameLost((AckGameLost) command);
                	} else if (command instanceof AckHit) {
                		processAckHit((AckHit) command);
                	} else if (command instanceof AckName) {
                		processAckName((AckName) command);
                	} else if (command instanceof AckSetup) {
                		processAckSetup((AckSetup) command);
                	}
                	//System.out.println(inputLine);	
                }
                localPlayer.setOnlinePlayerHandler(null);
                // Close the input and output streams and the client socket
                in.close();
                out.close();
                onlinePlayerSocket.close();
        	} catch (IOException e) {
				// ignored
			}
        }
        
        private void processRequestHit(ResponseCoordinate responseCoordinate) {
        	ResponseHit lastResponseHit = null;
        	for (ResponseHit responseHit : localPlayer.LAST_RESPONSE_HIT) {
        		if (responseHit.getId() == responseCoordinate.getId()) {
        			lastResponseHit = responseHit;
        			break;
        		}
        	}
        	
        	if (lastResponseHit != null) {
        		out.println(lastResponseHit.serialize());
				out.flush();
				
				localPlayer.LAST_RESPONSE_HIT.remove(lastResponseHit);
        	} else {
        		out.println(new ResponseNull().serialize());
				out.flush();
        	}
		}
        
        private void processRequestGameLost(RequestGameLost requestGameLost) {
        	ResponseGameLost lastResponseGameLost = null;
        	for (ResponseGameLost responseGameLost : localPlayer.LAST_RESPONSE_GAME_LOST) {
        		if (responseGameLost.getId() == requestGameLost.getId()) {
        			lastResponseGameLost = responseGameLost;
        			break;
        		}
        	}
        	
        	if (lastResponseGameLost != null) {
        		out.println(lastResponseGameLost.serialize());
        		out.flush();
        		
        		localPlayer.LAST_RESPONSE_GAME_LOST.remove(lastResponseGameLost);
        	} else {
        		out.println(new ResponseNull().serialize());
				out.flush();
        	}
        }
        
        private void processRequestCoordinate(RequestCoordinate requestCoordinate) {
        	ResponseCoordinate lastResponseCoordinate = null;
        	for (ResponseCoordinate responseCoordinate : localPlayer.LAST_RESPONSE_COORDINATE) {
        		if (responseCoordinate.getId() == requestCoordinate.getId()) {
        			lastResponseCoordinate = responseCoordinate;
        			break;
        		}
        	}
        	
        	if (lastResponseCoordinate != null) {
        		out.println(lastResponseCoordinate.serialize());
        		out.flush();
        		
        		localPlayer.LAST_RESPONSE_COORDINATE.remove(lastResponseCoordinate);
        	} else {
        		out.println(new ResponseNull().serialize());
				out.flush();
        	}
        }
        
        private void processRequestName(RequestName requestName) {
			out.println(new ResponseName(requestName.getId(), 
					localPlayer.getName()).serialize());
			out.flush();
		}
        
        private void processRequestSetup(RequestSetup requestSetup) {
        	ResponseSetup lastResponseSetup = null;
        	for (ResponseSetup responseSetup : localPlayer.LAST_RESPONSE_SETUP) {
        		if (responseSetup.getId() == requestSetup.getId()) {
        			lastResponseSetup = responseSetup;
        			break;
        		}
        	}
        	
        	if (lastResponseSetup != null) {
	    		out.println(lastResponseSetup.serialize());
	    		out.flush();
        	} else {
        		out.println(new ResponseNull().serialize());
				out.flush();
        	}
        }
        
        private void processAckCoordinate(AckCoordinate ackCoordinate) {
        	ResponseCoordinate lastResponseCoordinate = null;
        	for (ResponseCoordinate responseCoordinate : localPlayer.LAST_RESPONSE_COORDINATE) {
        		if (responseCoordinate.getId() == ackCoordinate.getId()) {
        			lastResponseCoordinate = responseCoordinate;
        			break;
        		}
        	}
        	
        	if (lastResponseCoordinate != null) {
                localPlayer.LAST_RESPONSE_COORDINATE.remove(lastResponseCoordinate);
        	}
        }
        
        private void processAckGameLost(AckGameLost ackGameLost) {
        	ResponseGameLost lastResponseGameLost = null;
        	for (ResponseGameLost responseGameLost : localPlayer.LAST_RESPONSE_GAME_LOST) {
        		if (responseGameLost.getId() == ackGameLost.getId()) {
        			lastResponseGameLost = responseGameLost;
        			break;
        		}
        	}
        	
        	if (lastResponseGameLost != null) {
        		localPlayer.LAST_RESPONSE_GAME_LOST.remove(lastResponseGameLost);
        	}
        }
        
        private void processAckHit(AckHit ackHit) {     	
        	ResponseHit lastResponseHit = null;
        	for (ResponseHit responseHit : localPlayer.LAST_RESPONSE_HIT) {
        		if (responseHit.getId() == ackHit.getId()) {
        			lastResponseHit = responseHit;
        			break;
        		}
        	}
        	
        	if (lastResponseHit != null) {
        		localPlayer.LAST_RESPONSE_HIT.remove(lastResponseHit);
        	}
        }
        
        private void processAckName(AckName ackName) {
        	
        }
        
        private void processAckSetup(AckSetup ackSetup) {
        	ResponseSetup lastResponseSetup = null;
        	for (ResponseSetup responseSetup : localPlayer.LAST_RESPONSE_SETUP) {
        		if (responseSetup.getId() == ackSetup.getId()) {
        			lastResponseSetup = responseSetup;
        			break;
        		}
        	}
        	
        	if (lastResponseSetup != null) {
    	    	localPlayer.LAST_RESPONSE_SETUP.remove(lastResponseSetup);
        	}
        }
        
        public LocalPlayer getLocalPlayer() {
        	return localPlayer;
        }
    }
}