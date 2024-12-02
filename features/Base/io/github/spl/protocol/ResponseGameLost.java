package io.github.spl.protocol;

public class ResponseGameLost implements Command {

    private boolean isLost = false;

	public ResponseGameLost(boolean isLost) {
		this.isLost = isLost;
	}

    @Override
	public String serialize() {
		return "L:" + (isLost ? "Y" : "N") + ".";
	}

	public boolean isLost() {
		return isLost;
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseGameLost)) {
        	return false;
        }

        return this.isLost == ((ResponseGameLost) other).isLost;
    }
}
