package io.github.spl.protocol;

public class RequestGameLost extends Command {
	
	public RequestGameLost(int id) {
		super(id);
	}
	
    @Override
	public String serialize() {
		return super.serialize() + "L:?.";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestGameLost && 
        		super.equals(other);
    }
}
