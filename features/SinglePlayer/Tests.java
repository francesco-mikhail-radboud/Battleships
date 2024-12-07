import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    @Test
    public void testCoordToHitGeneratesValidCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<>(), mockGrid, null);

        Coordinate coordinate = mockAIPlayer.coordToHit();
        assertTrue( coordinate.getX() >= 0 && coordinate.getX() < mockGrid.getDimension().getWidth()
        );
        assertTrue(coordinate.getY() >= 0 && coordinate.getY() < mockGrid.getDimension().getHeight());
    }

    @Test
    public void testGetRandomHitWithinBounds() {
        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<>(), null, null);

        for (int i = 0; i < 100; i++) {
            int randomValue = mockAIPlayer.getRandomHit(10);
            assertTrue(randomValue >= 0 && randomValue < 10);
        }
    }

    @Test
    public void testCheckListHitsReturnsFalseForHitCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        ShipCoordinate hitCoordinate = new ShipCoordinate(3, 5);
        mockGrid.getListOfCoordsHit().add(hitCoordinate);

        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<>(), mockGrid, null);

        assertFalse(mockAIPlayer.checkListHits(3, 5, mockGrid));
    }

    @Test
    public void testCheckListHitsReturnsTrueForUnhitCoordinates() {
        GameGrid mockGrid = new GameGrid(new Dimension(10, 10));
        ShipCoordinate hitCoordinate = new ShipCoordinate(3, 5);
        mockGrid.getListOfCoordsHit().add(hitCoordinate);

        AIPlayer mockAIPlayer = new AIPlayer("MockAI", new ArrayList<>(), mockGrid, null);

        assertTrue(mockAIPlayer.checkListHits(4, 4, mockGrid));
    }

    @Test
    public void testCanPlaceShipWithinBounds() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(2, 2), new Coordinate(2, 3))),
                new Coordinate(2, 2), 0
        );

        assertTrue(GameView.canPlaceShip(ship, existingShips, grid));
    }

    @Test
    public void testCanPlaceShipOutOfBounds() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(9, 9), new Coordinate(9, 10))),
                new Coordinate(9, 9), 0
        );

        assertFalse(GameView.canPlaceShip(ship, existingShips, grid));
    }

    @Test
    public void testCanPlaceShipOverlapWithExistingShip() {
        GameGrid grid = new GameGrid(new Dimension(10, 10));
        List<Ship> existingShips = new ArrayList<>();
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
        List<Ship> existingShips = new ArrayList<>();

        Ship ship = new Ship(
                new ShipTemplate("TestShip", Arrays.asList(new Coordinate(0, 0), new Coordinate(0, 1))),
                new Coordinate(0, 0), 0
        );

        assertTrue(GameView.canPlaceShip(ship, existingShips, grid));
    }
}
