import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

public class Tests {
	@Test
	public void testProtocolParser() {
		assertEquals(new RequestCoordinate(0) , ProtocolParser.parse("0:C:?."));
		
		assertEquals(new RequestGameLost(0), ProtocolParser.parse("0:L:?."));
		
		assertEquals(new RequestSetup(0), ProtocolParser.parse("0:SETUP:?."));
		
		assertEquals(new ResponseCoordinate(0, 1, 2), ProtocolParser.parse("0:C:1;2."));
		
		assertEquals(new ResponseGameLost(10, true), ProtocolParser.parse("10:L:Y."));
		assertEquals(new ResponseGameLost(0, false), ProtocolParser.parse("0:L:N."));
		
		assertEquals(new ResponseHit(0, ResponseHitOption.HIT, "name"), ProtocolParser.parse("0:H:name."));
		assertEquals(new ResponseHit(12, ResponseHitOption.MISS, null), ProtocolParser.parse("12:M."));
		assertEquals(new ResponseHit(5, ResponseHitOption.SINK, "name"), ProtocolParser.parse("5:S:name."));
		
		assertEquals(new ResponseSetup(9, true), ProtocolParser.parse("9:SETUP:Y."));
		assertEquals(new ResponseSetup(11, false), ProtocolParser.parse("11:SETUP:N."));
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
}
