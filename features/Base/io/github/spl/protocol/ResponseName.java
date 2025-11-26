package io.github.spl.protocol;

public class ResponseName extends Command {
	
	private String name;

	public ResponseName(int id, String name) {
		super(id);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
    @Override
	public String serialize() {
		return super.serialize() + "N:" + name + ".";
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ResponseName)) {
        	return false;
        }
        
        ResponseName otherResponseName = (ResponseName) other;

        return name.equals(otherResponseName.name) && 
        		super.equals(other);
    }
}