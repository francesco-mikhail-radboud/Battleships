package io.github.spl.protocol; 

/**
 * TODO description
 */
public  class  ResponseHit  extends Command {
	
	
	public static enum  ResponseHitOption {
		HIT ,  MISS ,  SINK}

	

	private final ResponseHitOption option;

	
	private final String name;

	
	
	public ResponseHit(int id, ResponseHitOption option, String name) {
		super(id);
		this.option = option;
		this.name = name;
	}

	
	
	@Override
	public String serialize() {
		switch (option) {
			case HIT: {
				return super.serialize() + "H:" + name + ".";
			}
			case MISS: {
				return super.serialize() + "M.";
			}
			case SINK: {
				return super.serialize() + "S:" + name + ".";
			}
		}
		
		return null;
	}

	

	public ResponseHitOption getHitOption() {
		return option;
	}

	

	public String getShipName() {
		return name;
	}

	
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseHit)) {
        	return false;
        }
        
        ResponseHit otherResponseHit = (ResponseHit) other;
        
        if (this.name == null) {
        	if (otherResponseHit.name == null) {
        		return this.option.equals(otherResponseHit.option);
        	} else {
        		return false;
        	}
        }
        
        return this.name.equals(otherResponseHit.name) && 
        		this.option.equals(otherResponseHit.option) && 
        		super.equals(other);
    }


}
