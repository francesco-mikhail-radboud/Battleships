package io.github.spl.protocol;

public class RequestGameLost implements Command {
    @Override
	public String serialize() {
		return "L:?.";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestGameLost;
    }
}
