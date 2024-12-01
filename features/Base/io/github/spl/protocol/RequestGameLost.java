package io.github.spl.protocol;

public class RequestGameLost implements Command {
    @Override
	public String serialize() {
		return "L:?.";
	}
}
