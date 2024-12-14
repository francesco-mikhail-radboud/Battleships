package io.github.spl.protocol; 

/**
 * TODO description
 */
public  class  AckGameLost  extends Command {
	
	
	public AckGameLost(int id) {
		super(id);
	}

	
	
	@Override
	public String serialize() {
		return super.serialize() + "ACK_L";
	}

	
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AckGameLost && 
        		super.equals(other);
    }


}
