package io.github.spl.game; 

import java.util.List; 
import io.github.spl.ships.*; 

public  class  CLIDisplay {
	

    public static final char SHIP = '☐';

	
    public static final char WATER = '~';

	
    public static final char SHIPHIT = 'X';

	
    public static final char WATERHIT = 'O';

	 

    public static void displayYourGrid(List<Ship> ships, Dimension boardDimension) {
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";   // Color water 
        final String YELLOW = "\u001B[33m"; // Color ships
        final String RED = "\u001B[31m";    // Color hit ships
        final String WHITE = "\u001B[37m";  // Color hit water

        int width = boardDimension.getWidth();
        int height = boardDimension.getHeight();

        System.out.println("  " + "Your Battleship Board: ");

        System.out.print("   ");
        for (int col = 0; col < width; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < height; row++) {
            System.out.printf("%2d ", row); 

            for (int col = 0; col < width; col++) {
                ShipCoordinate current = new ShipCoordinate(row, col);

                ShipCoordinate shipCoord = null;
                for (Ship ship : ships){
                    for (ShipCoordinate sc : ship.getShipCoordinates()){
                        if (sc.equals(current)) {
                            shipCoord = sc;
                            break;
                        }
                    }
                }
            
                if (shipCoord != null) {
                    if (shipCoord.getIsHit()) {
                        System.out.print(RED + SHIPHIT +" " + RESET); 
                    } else {
                        System.out.print(YELLOW + SHIP + " " + RESET);  
                    }
                } else {
                    System.out.print(BLUE + WATER + " " + RESET); 
                }
            }
            System.out.println();
        }
    }

	


    public static void displayGridHits(List<ShipCoordinate> listOfCoordsHit, Dimension boardDimension) {
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";   // Color water 
        final String YELLOW = "\u001B[33m"; // Color ships
        final String RED = "\u001B[31m";    // Color hit ships
        final String WHITE = "\u001B[37m";  // Color hit water

        int width = boardDimension.getWidth();
        int height = boardDimension.getHeight();

        System.out.println("  " + "Battleship Opponent Board: ");

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
                ShipCoordinate hitCoord = null;
                for (ShipCoordinate coord : listOfCoordsHit){
                    if(coord.equals(current)){
                        hitCoord = coord; 
                        break;
                    }
                }

                if (hitCoord != null) {
                    if (hitCoord.getIsHit()) {
                        System.out.print(RED + SHIPHIT +" " + RESET); 
                    } else {
                        System.out.print(WHITE + WATERHIT + " " + RESET); 
                    }
                } else {
                    System.out.print(BLUE + WATER + " " + RESET); 
                }
                    
            }
            System.out.println();
        }        
    }


}
