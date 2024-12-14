package io.github.spl.protocol;

/**
 * TODO description
 */
public class AckName extends Command {
	
	public AckName(int id) {
		super(id);
	}
	
	@Override
	public String serialize() {
		return super.serialize() + "ACK_N";
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AckName && 
        		super.equals(other);
    }
}