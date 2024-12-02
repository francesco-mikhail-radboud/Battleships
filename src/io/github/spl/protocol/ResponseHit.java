package io.github.spl.protocol; 

/**
 * TODO description
 */
public  class  ResponseHit  implements Command {
	
	
	public static enum  ResponseHitOption {
		HIT ,  MISS ,  SINK}

	

	private final ResponseHitOption option;

	
	private final String name;

	
	
	public ResponseHit(ResponseHitOption option, String name) {
		this.option = option;
		this.name = name;
	}

	
	
	@Override
	public String serialize() {
		switch (option) {
			case HIT: {
				return "H:" + name + ".";
			}
			case MISS: {
				return "M.";
			}
			case SINK: {
				return "S:" + name + ".";
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
        
        return this.name.equals(otherResponseHit.name) && this.option.equals(otherResponseHit.option);
    }


}
