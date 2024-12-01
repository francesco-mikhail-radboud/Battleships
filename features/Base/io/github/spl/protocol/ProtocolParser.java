package io.github.spl.protocol;

import io.github.spl.exceptions.UnknownCommandException;
import io.github.spl.protocol.ResponseHit.ResponseHitOption;

/**
 * TODO description
 */
public class ProtocolParser {
	public static Command parse(String command) {
		if (command.length() > 0) {
			if (command.charAt(0) == 'C') {
				int semicolPos = command.indexOf(';');
				int dotPos = command.length() - 1;
				try {
					int x = Integer.parseInt(command.substring(2, semicolPos));
					int y = Integer.parseInt(command.substring(semicolPos + 1, dotPos));
					return new ResponseCoordinate(x, y);
				} catch (Exception exception) {
					throw new UnknownCommandException(command);
				}
			} else if (command.charAt(0) == 'S') {
				try {
					String name = command.substring(2, command.length() - 1);
					return new ResponseHit(ResponseHitOption.SINK, name);
				} catch (Exception e) {
					throw new UnknownCommandException(command);
				}
			} else if (command.charAt(0) == 'H') {
				try {
					String name = command.substring(2, command.length() - 1);
					return new ResponseHit(ResponseHitOption.HIT, name);
				} catch (Exception e) {
					throw new UnknownCommandException(command);
				}
			} else if (command.equals("C:?.")) {
				return new RequestCoordinate();
			} else if (command.equals("M.")) {
				return new ResponseHit(ResponseHitOption.MISS, "");
			} else if (command.equals("L:Y.")) {
                return new ResponseGameLost(true);
			} else if (command.equals("L:N.")) {
                return new ResponseGameLost(false);
			} else if (command.equals("L:?.")) {
				return new RequestGameLost();
			} else if (command.equals("SETUP:?.")) {
				return new RequestSetup();
			} else if (command.equals("SETUP:Y.")) {
				return new ResponseSetup(true);
			} else if (command.equals("SETUP:N.")) {
				return new ResponseSetup(false);
			}
		} else {
			throw new UnknownCommandException(command);
		}
		
		return null;
	}
}