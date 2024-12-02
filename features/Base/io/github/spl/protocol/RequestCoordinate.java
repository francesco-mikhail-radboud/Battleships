package io.github.spl.protocol;

import io.github.spl.ships.Coordinate;

public class RequestCoordinate implements Command {

    @Override
	public String serialize() {
		return "C:?.";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestCoordinate;
    }
}
