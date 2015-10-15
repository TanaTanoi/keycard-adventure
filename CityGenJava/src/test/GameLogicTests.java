package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import gameObjects.objects.Entity;
import gameObjects.objects.Portal;
import gameObjects.objects.Weapon;
import gameObjects.player.Player;
import gameObjects.world.Floor;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;
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
		HelperMethods.createFloor(g);
		HelperMethods.addWeapon(g);

		Floor f = g.getFloor(1);
		assert f.getEntities().size() == 1;
	}

	@Test
	public void testPickUp(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.createFloor(g);
		Weapon w = HelperMethods.addWeapon(g);

		Floor f = g.getFloor(1);
		g.pickUpItem(g.getPlayers().get(1).getID(),w.getID());

		assert f.getEntities().size() == 0;
		assert g.getPlayers().get(1).getInventory()[0].equals(w);
	}


	@Test
	public void attackPlayer(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.createFloor(g);

		Player p = g.getPlayers().get(1);
		p.attack(50);

		assert p.getHealth() == 50;

	}

	@Test
	public void takePotion(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.createFloor(g);

		Player p = g.getPlayers().get(1);
		p.takePotion(-20);

		assert p.getHealth() == 80;

	}

	@Test
	public void testClosestEntity(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.createFloor(g);
		int iD = g.addNewPlayer("Bobby");
		Player bob = g.getPlayers().get(iD);
		bob.setLocation(new Location(1,0,1));
		Weapon w = HelperMethods.addWeapon(g);

		Entity e = g.closestEntity(bob.getLocation(), 3.0f , 0);
		assert e ==w;
	}

	@Test
	public void testMoveFloor(){
		GameWorld g = HelperMethods.setupGame();
		View view = new View(g,null);
		HelperMethods.createFloor(g);
		HelperMethods.createSecondFloor(g);
		HelperMethods.addPortal(g);

		Portal portal = (Portal)g.getFloor(1).getEntity(0);

		Player p = g.getPlayers().get(1);
		g.moveFloor(p.getID(), portal.getID());

		assert p.getLocation().getFloor() == 2;

	}


}
