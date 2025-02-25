package io.github.spl.player; 

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.PrintWriter; 
import java.net.Socket; 

import io.github.spl.game.GameGrid; 
import io.github.spl.game.GameView; 
import io.github.spl.player.Player; 
import io.github.spl.protocol.*; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.ShipCoordinate; 

import java.net.Socket; 

public  class  OnlinePlayer  implements Player {
	
	
	private static int REFRESH_RATE = 100;

	
	
	private String name;

	
	private Socket socket;

	
    private PrintWriter out;

	
    private BufferedReader in;

	
    
    private GameView gameView;

	
	
	public OnlinePlayer(Socket socket, GameView gameView) {
		this.name = null;
		try {
			this.socket = socket;	
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		this.gameView = gameView;
	}

	
	
	public ResponseHit hit(Coordinate coordinate) {
		try {
			out.println(new ResponseCoordinate(gameView.getGame().getStep(), 
					coordinate.getX(), coordinate.getY()).serialize());
			out.flush();
			String response;
			while (ProtocolParser.parse((response = in.readLine())) instanceof ResponseNull) {
				out.println(new ResponseCoordinate(gameView.getGame().getStep(), 
						coordinate.getX(), coordinate.getY()).serialize());
				out.flush();
				Thread.sleep(REFRESH_RATE);
			}
			Command result = ProtocolParser.parse(response);
			if (result instanceof ResponseHit) {
				out.println(new AckHit(gameView.getGame().getStep()).serialize());
				out.flush();

				return (ResponseHit) result;
			}
			return null;
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e1) {
			return null;
		}
	}

	
	
    public ResponseGameLost isGameLost() {
    	try {
			out.println(new RequestGameLost(gameView.getGame().getStep()).serialize());
			out.flush();
			String response;
			while (ProtocolParser.parse((response = in.readLine())) instanceof ResponseNull) {
				out.println(new RequestGameLost(gameView.getGame().getStep()).serialize());
				out.flush();
				Thread.sleep(REFRESH_RATE);
			}
			Command result = ProtocolParser.parse(response);
			if (result instanceof ResponseGameLost) {
				out.println(new AckGameLost(gameView.getGame().getStep()).serialize());
				out.flush();
				return (ResponseGameLost) result;
			}
			return null;
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e1) {
			return null;
		}
    }

	
    
	public ResponseCoordinate selectCoordinate() {
    	try {
			out.println(new RequestCoordinate(gameView.getGame().getStep()).serialize());
			out.flush();
			String response;
			while (ProtocolParser.parse((response = in.readLine())) instanceof ResponseNull) {
				out.println(new RequestCoordinate(gameView.getGame().getStep()).serialize());
				out.flush();
				Thread.sleep(REFRESH_RATE);
			}
			Command result = ProtocolParser.parse(response);
			if (result instanceof ResponseCoordinate) {
				out.println(new AckCoordinate(gameView.getGame().getStep()).serialize());
				out.flush();
				return (ResponseCoordinate) result;
			}
			return null;
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e1) {
			return null;
		}
	}

	
	
	public String getName() {
		if (name == null) {
			try {
				out.println(new RequestName(gameView.getGame().getStep()).serialize());
				out.flush();
				String response;
				while (ProtocolParser.parse((response = in.readLine())) instanceof ResponseNull) {
					out.println(new RequestName(gameView.getGame().getStep()).serialize());
					out.flush();
					Thread.sleep(REFRESH_RATE);
				}
				Command result = ProtocolParser.parse(response);
				if (result instanceof ResponseName) {
					out.println(new AckName(gameView.getGame().getStep()).serialize());
					out.flush();
					
					name = ((ResponseName) result).getName();
					return name;
				}
				return null;
			} catch (IOException e) {
				return null;
			} catch (InterruptedException e1) {
				return null;
			}
		} else {
			return name;
		}
	}

	
	
	public ResponseSetup setup() {
    	try {
			out.println(new RequestSetup(gameView.getGame().getStep()).serialize());
			out.flush();
			String response;
			while (ProtocolParser.parse((response = in.readLine())) instanceof ResponseNull) {
				out.println(new RequestSetup(gameView.getGame().getStep()).serialize());
				out.flush();
				Thread.sleep(REFRESH_RATE);
			}
			Command result = ProtocolParser.parse(response);
			if (result instanceof ResponseSetup) {
				out.println(new AckSetup(gameView.getGame().getStep()).serialize());
				out.flush();
				return (ResponseSetup) result;
			}
			return null;
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e1) {
			return null;
		}
	}


}
