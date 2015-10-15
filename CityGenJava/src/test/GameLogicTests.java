package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import gameObjects.world.Floor;
import gameObjects.world.GameWorld;
import gameObjects.world.Parser;
import graphics.View;

import org.junit.Test;

/**
 * These tests check whether the logic of the classes within the gameObjects
 * package work
 *
 * @author craighhann
 */
public class GameLogicTests {

	/**
	 * This tests checks whether the parser correctly parses a game file.
	 * The test file we are checking it against contains one of each kind of
	 * object our parser has to be able to handle
	 */
	@Test
	public void testParser(){
		try{
			GameWorld g = HelperMethods.setupGame();
			View view = new View(g,null);
			Parser.parseWorld("testConfig.txt",g);
		}catch(FileNotFoundException e){assert false;}
		assert true;
	}

	@Test
	public void testItemInWorld(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.addWeapon(g);

		Floor f = g.getFloor(1);
		assert f.getEntities().size() == 1;
	}


}
