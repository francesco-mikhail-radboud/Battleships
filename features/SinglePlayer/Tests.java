import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import io.github.spl.ships.Ship;
import io.github.spl.game.*;
import io.github.spl.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Tests {
    @Test
    public void testCoordToHitGeneratesValidCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<Ship>(), mockGrid, null);

        Coordinate coordinate = mockAIPlayer.getHighestProbabilityCoordinate();
        assertTrue( coordinate.getX() >= 0 && coordinate.getX() < mockGrid.getDimension().getWidth());
        assertTrue(coordinate.getY() >= 0 && coordinate.getY() < mockGrid.getDimension().getHeight());
    }

    @Test
    public void testCheckListHitsReturnsFalseForHitCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        ShipCoordinate hitCoordinate = new ShipCoordinate(3, 5);
        mockGrid.getListOfCoordsHit().add(hitCoordinate);

        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<Ship>(), mockGrid, null);

        assertFalse(LocalPlayer.checkListHits(3, 5, mockGrid));
    }

    @Test
    public void testCheckListHitsReturnsTrueForUnhitCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        ShipCoordinate hitCoordinate = new ShipCoordinate(3, 5);
        mockGrid.getListOfCoordsHit().add(hitCoordinate);

        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<Ship>(), mockGrid, null);

        assertTrue(LocalPlayer.checkListHits(4, 4, mockGrid));
    }

    @Test
    public void testCanPlaceShipWithinBounds() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<Ship>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(2, 2), new Coordinate(2, 3))),
                new Coordinate(2, 2), 0
        );

        assertTrue(GameView.canPlaceShip(ship, existingShips, grid));
    }

    @Test
    public void testCanPlaceShipOutOfBounds() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<Ship>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(9, 9), new Coordinate(9, 10))),
                new Coordinate(9, 9), 0
        );

        assertFalse(GameView.canPlaceShip(ship, existingShips, grid));
    }

    @Test
    public void testCanPlaceShipOverlapWithExistingShip() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<Ship>();
        Ship existingShip = new Ship(
                new ShipTemplate("ExistingShip", Arrays.asList(new Coordinate(3, 3), new Coordinate(3, 4))),
                new Coordinate(3, 3), 0
        );
        existingShips.add(existingShip);

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(3, 3), new Coordinate(3, 5))),
                new Coordinate(3, 3), 0
        );

        assertFalse(GameView.canPlaceShip(ship, existingShips, grid));
    }

    @Test
    public void testCanPlaceShipOnEmptyGrid() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<Ship>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(0, 0), new Coordinate(0, 1))),
                new Coordinate(0, 0), 0
        );

        assertTrue(GameView.canPlaceShip(ship, existingShips, grid));
    }
}
