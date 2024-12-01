package io.github.spl.protocol; 

public  class  RequestSetup  implements Command {
	
    @Override
	public String serialize() {
		return "SETUP:?.";
	}


}
