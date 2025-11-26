package io.github.spl.exceptions; 

public  class  UnknownCommandException  extends BattleshipException {
	 
	public UnknownCommandException(String command) {
		super("Unknown command \"" + command + "\"");
	}


}
