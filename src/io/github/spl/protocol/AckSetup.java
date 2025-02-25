package io.github.spl.protocol; 

public  class  AckSetup  extends Command {
	
	
	public AckSetup(int id) {
		super(id);
	}

	
	
	@Override
	public String serialize() {
		return super.serialize() + "ACK_SETUP";
	}

	
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AckSetup && 
        		super.equals(other);
    }


}
