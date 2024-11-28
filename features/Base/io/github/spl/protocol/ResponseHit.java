package io.github.spl.protocol;

/**
 * TODO description
 */
public class ResponseHit implements Command {
	
	public static enum ResponseHitOption {
		HIT, MISS, SINK
	}

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
}