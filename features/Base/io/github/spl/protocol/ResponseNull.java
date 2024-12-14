package io.github.spl.protocol;

public class ResponseNull extends Command {
	
	public ResponseNull() {
		super(0);
	}
	
    @Override
	public String serialize() {
		return super.serialize() + "NULL";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof ResponseNull && 
        		super.equals(other);
    }
}