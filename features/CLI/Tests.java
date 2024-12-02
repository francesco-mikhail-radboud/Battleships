import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.spl.game.CLIGameView;
import io.github.spl.game.Dimension;
import io.github.spl.game.GameGrid;
import io.github.spl.ships.Coordinate;
import io.github.spl.ships.ShipCoordinate;

public class Tests {
	@Test
	public void testCLIGameView_checkListHits() {
		CLIGameView cliGameView = new CLIGameView();
		
		GameGrid gameGrid = new GameGrid(new Dimension(10, 10));
		
		gameGrid.add(new ShipCoordinate(new Coordinate(0, 0)));
		
		assertFalse(cliGameView.checkListHits(0, 0, gameGrid));
		
		assertTrue(cliGameView.checkListHits(1, 1, gameGrid));
	}
}
