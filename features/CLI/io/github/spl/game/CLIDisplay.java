public class CLIDisplay {

    public static final char SHIP = '☐';
    public static final char WATER = '~';
    public static final char SHIPHIT = 'X';
    public static final char WATERHIT = 'O';

    public void displayGridWithHitsCLI(List<Ship> ships, List<ShipCoordinate> listOfCoordsHit, 
                                        Dimension boardDimension, boolean isGridOwner) {
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";   // Color water 
        final String YELLOW = "\u001B[33m"; // Color ships
        final String RED = "\u001B[31m";    // Color hit ships
        final String WHITE = "\u001B[37m";  // Color hit water

        int width = boardDimension.getWidth();
        int height = boardDimension.getHeight();

        System.out.println("  " + " ".repeat(width) + "Battleship Board");

        System.out.print("   ");
        for (int col = 0; col < width; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < height; row++) {
            System.out.printf("%2d ", row); 

            for (int col = 0; col < width; col++) {
                ShipCoordinate current = new ShipCoordinate(row, col);

                // check if it's a hit 
                ShipCoordinate hitCoord = listOfCoordsHit.stream()
                    .filter(coord -> coord.equals(current))
                    .findFirst()
                    .orElse(null);

                if (hitCoord != null) {
                    if (hitCoord.getIsHit()) {
                        System.out.print(RED + SHIPHIT +" " + RESET); 
                    } else {
                        System.out.print(WHITE + WATERHIT + " " + RESET); 
                    }
                } else {
                    if (isGridOwner) {
                        ShipCoordinate shipCoord = ships.stream()
                        .flatMap(ship -> ship.getShipCoordinates().stream())
                        .filter(sc -> sc.equals(current))
                        .findFirst()
                        .orElse(null);

                        if (shipCoord != null) {
                            System.out.print(YELLOW + SHIP + " " + RESET); 
                        } else {
                            System.out.print(BLUE + WATER + " " + RESET); 
                        }
                    } else {
                        System.out.print(BLUE + WATER + " " + RESET);
                    }
                    
                }
            }
            System.out.println();
        }
    }

}
