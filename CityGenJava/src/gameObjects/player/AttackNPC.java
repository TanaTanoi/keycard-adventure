package gameObjects.player;

import gameObjects.world.Location;

/**
 * An attackNPC is a type of NPC that has no dialogue.
 * It's only purpose is to be attacked by other players with weapons
 * they may have. In future, they could drop items when they die
 *
 * @author craighhann
 *
 */
public class AttackNPC extends NPC{

	public AttackNPC(String name, Location loc, int ID, String modelName) {
		super(name, loc, ID, modelName);
	}

}
