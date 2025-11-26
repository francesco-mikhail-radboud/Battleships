package io.github.spl.protocol; 

public  class  AckHit  extends Command {
	
	
	public AckHit(int id) {
		super(id);
	}

	
	
	@Override
	public String serialize() {
		return super.serialize() + "ACK_H";
	}

	
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        return other instanceof AckHit && 
        		super.equals(other);
    }


}
