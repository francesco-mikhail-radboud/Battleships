package io.github.spl.protocol;

public class RequestSetup extends Command {
	
	public RequestSetup(int id) {
		super(id);
	}
	
    @Override
	public String serialize() {
		return super.serialize() +  "SETUP:?.";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestSetup && 
        		super.equals(other);
    }
}
