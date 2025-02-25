package io.github.spl.protocol;

import io.github.spl.ships.Coordinate;

import java.util.List;

public class ResponseCoordinateList extends Command {

    private final List<Coordinate> coordinateList;

    public ResponseCoordinateList(int id, List<Coordinate> coordinateList) {
    	super(id);
        this.coordinateList = coordinateList;
    }

    @Override
    public String serialize() {
        StringBuilder coordResponseList = new StringBuilder("C:");
        for (Coordinate coord : coordinateList){
            coordResponseList.append(coord.getX())
                    .append(";")
                    .append(coord.getY());
            coordResponseList.append(";");
        }
        coordResponseList.setLength(coordResponseList.length() - 1);
        coordResponseList.append(".");
        return super.serialize() + coordResponseList.toString();
    }

    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ResponseCoordinateList)) {
            return false;
        }

        ResponseCoordinateList otherCommand = (ResponseCoordinateList) other;

        if (coordinateList.size() != otherCommand.getCoordinateList().size()) {
            return false;
        }

        for (int i = 0; i < coordinateList.size(); i++) {
            if (!coordinateList.get(i).equals(otherCommand.coordinateList.get(i))) {
                return false;
            }
        }

        return super.equals(other);
    }
}