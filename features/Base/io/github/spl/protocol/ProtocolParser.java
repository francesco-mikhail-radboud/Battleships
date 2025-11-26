package io.github.spl.protocol;

import io.github.spl.exceptions.UnknownCommandException;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;
import io.github.spl.ships.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class ProtocolParser {
	public static Command parse(String command) {
		if (command == null) {
			return null;
		}
		int index = command.indexOf(":");
		int id = Integer.parseInt(command.substring(0, index));
		command = command.substring(index + 1);
		if (command.length() > 0) {
			if (command.equals("C:?.")) {
				return new RequestCoordinate(id);
			} else if (command.equals("M.")) {
				return new ResponseHit(id, ResponseHitOption.MISS, null);
			} else if (command.equals("L:Y.")) {
                return new ResponseGameLost(id, true);
			} else if (command.equals("L:N.")) {
                return new ResponseGameLost(id, false);
			} else if (command.equals("L:?.")) {
				return new RequestGameLost(id);
			} else if (command.equals("SETUP:?.")) {
				return new RequestSetup(id);
			} else if (command.equals("SETUP:Y.")) {
				return new ResponseSetup(id, true);
			} else if (command.equals("SETUP:N.")) {
				return new ResponseSetup(id, false);
			} else if (command.equals("N:?.")) {
				return new RequestName(id);
			} else if (command.equals("NULL")) {
				return new ResponseNull();
			} else if (command.equals("ACK_C")) {
				return new AckCoordinate(id);
			} else if (command.equals("ACK_L")) {
				return new AckGameLost(id);
			} else if (command.equals("ACK_H")) {
				return new AckHit(id);
			} else if (command.equals("ACK_N")) {
				return new AckName(id);
			} else if (command.equals("ACK_SETUP")) {
				return new AckSetup(id);
			} else if (command.startsWith("C:") && command.endsWith(".")) {
				String data = command.substring(2, command.length() - 1);
				String[] parts = data.split(";");

				if (parts.length == 2) { // ResponseCoordinate
					try {
						int x = Integer.parseInt(parts[0]);
						int y = Integer.parseInt(parts[1]);
						return new ResponseCoordinate(id, x, y);
					} catch (NumberFormatException e) {
						throw new UnknownCommandException(command);
					}
				} else if (parts.length % 2 == 0) { // ResponseCoordinateList
					try {
						List<Coordinate> coordinates = new ArrayList<Coordinate>();
						for (int i = 0; i < parts.length; i += 2) {
							int x = Integer.parseInt(parts[i]);
							int y = Integer.parseInt(parts[i + 1]);
							coordinates.add(new Coordinate(x, y));
						}
						return new ResponseCoordinateList(id, coordinates);
					} catch (NumberFormatException e) {
						throw new UnknownCommandException(command);
					}
				}
			} else if (command.charAt(0) == 'S') {
				try {
					String name = command.substring(2, command.length() - 1);
					return new ResponseHit(id, ResponseHitOption.SINK, name);
				} catch (Exception e) {
					throw new UnknownCommandException(command);
				}
			} else if (command.charAt(0) == 'H') {
				try {
					String name = command.substring(2, command.length() - 1);
					return new ResponseHit(id, ResponseHitOption.HIT, name);
				} catch (Exception e) {
					throw new UnknownCommandException(command);
				}
			} else if (command.charAt(0) == 'N') {
				try {
					String playerName = command.substring(2, command.length() - 1);
					return new ResponseName(id, playerName);
				} catch (Exception e) {
					throw new UnknownCommandException(command);
				}
			}
		} else {
			throw new UnknownCommandException(command);
		}
		
		return null;
	}
}