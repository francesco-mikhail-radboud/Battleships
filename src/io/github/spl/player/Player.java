package io.github.spl.player; 

import java.awt.image.BufferedImage; 
import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.io.OutputStream; 
import java.util.ArrayList; 
import java.util.List; 

import io.github.spl.game.GameGrid; 
import io.github.spl.game.GameView; 
import io.github.spl.protocol.*; 
import io.github.spl.ships.*; 

/**
 * TODO description
 */

public  interface  Player {
	
	ResponseHit hit(Coordinate coordinate);

	
    ResponseGameLost isGameLost();

	
	ResponseCoordinate selectCoordinate();

	
	String getName();


}
