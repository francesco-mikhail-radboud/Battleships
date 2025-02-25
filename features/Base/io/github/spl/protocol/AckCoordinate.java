package io.github.spl.protocol;

public class AckCoordinate extends Command {
	
	public AckCoordinate(int id) {
		super(id);
	}
	
	@Override
	public String serialize() {
		return super.serialize() + "ACK_C";
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AckCoordinate && 
        		super.equals(other);
    }
}