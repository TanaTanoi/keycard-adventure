package gameObjects.player;

import gameObjects.world.Location;

/**
 * The goal of the game is to find the "Trophy NPC" and click on it
 * This will display a message saying you have won the game
 *
 * @author craighhann
 *
 */

public class TrophyNPC extends InfoNPC {

	public TrophyNPC(String name, Location loc, String information,
			String modelName, int ID) {
		super(name, loc, information, modelName, ID);
	}

}
