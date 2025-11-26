package io.github.spl.protocol; 

import io.github.spl.ships.Coordinate; 

public  class  RequestCoordinate  extends Command {
	

	public RequestCoordinate(int id) {
		super(id);
	}

	
	
    @Override
	public String serialize() {
		return super.serialize() + "C:?.";
	}

	
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestCoordinate && 
        		super.equals(other);
    }


}
