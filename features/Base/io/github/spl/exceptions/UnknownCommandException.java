package io.github.spl.exceptions;

/**
 * TODO description
 */
public class UnknownCommandException extends BattleshipException { 
	public UnknownCommandException(String command) {
		super("Unknown command \"" + command + "\"");
	}
}