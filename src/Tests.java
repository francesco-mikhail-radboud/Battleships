import static org.junit.Assert.assertFalse; import static org.junit.Assert.assertTrue; 
import static org.junit.jupiter.api.Assertions.assertEquals; 

import java.util.ArrayList; 
import java.util.List; 

import org.junit.jupiter.api.Test; 

import io.github.spl.game.Game; 
import io.github.spl.game.GameType; 
import io.github.spl.protocol.*; 
import io.github.spl.protocol.ResponseHit.ResponseHitOption; 
import io.github.spl.ships.Coordinate; 
import io.github.spl.ships.Ship; 
import io.github.spl.ships.ShipCoordinate; 
import io.github.spl.ships.ShipTemplate; 

import io.github.spl.game.CLIGameView; 
import io.github.spl.game.Dimension; 
import io.github.spl.game.GameGrid; public   class  Tests {
	
	@Test
	public void testProtocolParser() {
		assertEquals(new RequestCoordinate() , ProtocolParser.parse("C:?."));
		
		assertEquals(new RequestGameLost(), ProtocolParser.parse("L:?."));
		
		assertEquals(new RequestSetup(), ProtocolParser.parse("SETUP:?."));
		
		assertEquals(new ResponseCoordinate(1, 2), ProtocolParser.parse("C:1;2."));
		
		assertEquals(new ResponseGameLost(true), ProtocolParser.parse("L:Y."));
		assertEquals(new ResponseGameLost(false), ProtocolParser.parse("L:N."));
		
		assertEquals(new ResponseHit(ResponseHitOption.HIT, "name"), ProtocolParser.parse("H:name."));
		assertEquals(new ResponseHit(ResponseHitOption.MISS, null), ProtocolParser.parse("M."));
		assertEquals(new ResponseHit(ResponseHitOption.SINK, "name"), ProtocolParser.parse("S:name."));
		
		assertEquals(new ResponseSetup(true), ProtocolParser.parse("SETUP:Y."));
		assertEquals(new ResponseSetup(false), ProtocolParser.parse("SETUP:N."));
	}

	
	
	@Test
	public void shipTemplatesTest() {
		List<ShipTemplate> shipTemplates = new ArrayList<ShipTemplate>();
		shipTemplates.add(new ShipTemplate("a", null));
		shipTemplates.add(new ShipTemplate("b", null));
		shipTemplates.add(new ShipTemplate("b", null));
		
		GameType gameType = new GameType(null, shipTemplates);
		Game game = new Game(gameType, null);
		
		assertEquals(shipTemplates.get(1), game.getShipTemplateByName("b"));
	}

	
	
	@Test
	public void checkCoordinatesEquals() {
		assertEquals(new Coordinate(1, 2), new Coordinate(1, 2));
	}

	
	
	@Test
	public void shipHitTest1() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.add(new Coordinate(0, 0));
		coordinates.add(new Coordinate(0, 1));
		coordinates.add(new Coordinate(0, 2));
		
		ShipTemplate shipTemplate = new ShipTemplate("a", coordinates);
		Ship ship = new Ship(shipTemplate, new Coordinate(0, 0), 0);
		
		boolean result = ship.hit(new Coordinate(0, 1));
		assertTrue(result);
		
		assertTrue(ship.getShipCoordinates().get(1).getIsHit());
	}

	
	
	@Test
	public void shipHitTest2() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.add(new Coordinate(0, 0));
		coordinates.add(new Coordinate(0, 1));
		coordinates.add(new Coordinate(0, 2));
		
		ShipTemplate shipTemplate = new ShipTemplate("a", coordinates);
		Ship ship = new Ship(shipTemplate, new Coordinate(0, 0), 0);
		
		boolean result = ship.hit(new Coordinate(1, 3));
		assertFalse(result);
		
		for (ShipCoordinate c : ship.getShipCoordinates()) {
			assertFalse(c.getIsHit());
		}
	}

	
	
	@Test
	public void sinkShipTest() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.add(new Coordinate(0, 0));
		coordinates.add(new Coordinate(0, 1));
		coordinates.add(new Coordinate(0, 2));
		
		ShipTemplate shipTemplate = new ShipTemplate("a", coordinates);
		Ship ship = new Ship(shipTemplate, new Coordinate(0, 0), 0);
		
		boolean result = ship.hit(new Coordinate(0, 0));
		assertTrue(result);
		
		result = ship.hit(new Coordinate(0, 1));
		assertTrue(result);
		
		result = ship.hit(new Coordinate(0, 2));
		assertTrue(result);
		
		assertTrue(ship.isSunk());
	}

	
	@Test
	public void testCLIGameView_checkListHits() {
		CLIGameView cliGameView = new CLIGameView();
		
		GameGrid gameGrid = new GameGrid(new Dimension(10, 10));
		
		gameGrid.add(new ShipCoordinate(new Coordinate(0, 0)));
		
		assertFalse(cliGameView.checkListHits(0, 0, gameGrid));
		
		assertTrue(cliGameView.checkListHits(1, 1, gameGrid));
	}


}
