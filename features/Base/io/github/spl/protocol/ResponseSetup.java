package io.github.spl.protocol;

public class ResponseSetup extends Command {
    private final boolean isSuccess;

    public ResponseSetup(int id, boolean isSuccess) {
    	super(id);
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
	public String serialize() {
		return super.serialize() + "SETUP:" + (isSuccess ? "Y" : "N") + ".";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseSetup)) {
        	return false;
        }

        return this.isSuccess == ((ResponseSetup) other).isSuccess 
        		&& super.equals(other);
    }
}
