package gameObjects.objects;

import gameObjects.world.Location;

/**
 * A map is a type of tool that when a player has it, they can see an outline of the level
 * It cannot be interacted with ob be 'used' on a player
 *
 * @author craighhann
 */

public class Map extends Tool{

	public Map(String name, String desc, Location loc, String model, int ID,
			String image) {
		super(name, desc, loc, model, ID, image);
	}
}
