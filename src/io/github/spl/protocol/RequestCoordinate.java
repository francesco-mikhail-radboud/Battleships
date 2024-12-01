package io.github.spl.protocol; 

import io.github.spl.ships.Coordinate; 

public  class  RequestCoordinate  implements Command {
	

    @Override
	public String serialize() {
		return "C:?.";
	}


}
