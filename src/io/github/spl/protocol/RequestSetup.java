package io.github.spl.protocol; 

public  class  RequestSetup  implements Command {
	
    @Override
	public String serialize() {
		return "SETUP:?.";
	}

	
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RequestSetup;
    }


}
