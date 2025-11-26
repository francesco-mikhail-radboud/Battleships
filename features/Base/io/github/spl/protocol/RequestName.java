package io.github.spl.protocol;

public class RequestName extends Command {
	
	public RequestName(int id) {
		super(id);
	}
	
    @Override
	public String serialize() {
		return super.serialize() + "N:?.";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestName && 
        		super.equals(other);
    }
}