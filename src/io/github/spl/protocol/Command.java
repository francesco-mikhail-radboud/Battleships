package io.github.spl.protocol; 

/**
 * TODO description
 */
public abstract  class  Command {
	
	protected int id;

	
	
	public Command(int id) {
		this.id = id;
	}

	
	
	protected String serialize() {
		return id + ":";
	}

	
	
	public boolean equals(Object other) {
		if (!(other instanceof Command)) {
			return false;
		}
		
		Command otherCommand = (Command) other;
		
		return id == otherCommand.id;
	}

	
	
	public int hashCode() {
		return serialize().hashCode();
	}

	
	
	public int getId() {
		return id;
	}


}
