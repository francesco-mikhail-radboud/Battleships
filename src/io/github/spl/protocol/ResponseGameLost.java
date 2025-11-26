package io.github.spl.protocol; 

public  class  ResponseGameLost  extends Command {
	

    private boolean isLost = false;

	

	public ResponseGameLost(int id, boolean isLost) {
		super(id);
		this.isLost = isLost;
	}

	

    @Override
	public String serialize() {
		return super.serialize() + "L:" + (isLost ? "Y" : "N") + ".";
	}

	

	public boolean isLost() {
		return isLost;
	}

	
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseGameLost)) {
        	return false;
        }

        return this.isLost == ((ResponseGameLost) other).isLost &&
        		super.equals(other);
    }


}
