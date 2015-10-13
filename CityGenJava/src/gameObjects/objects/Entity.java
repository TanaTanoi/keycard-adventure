package gameObjects.objects;

import gameObjects.world.Location;

/**
 * An entity is either an item or an NPC. It is something that a player can interact with.
 * In essence, it is every game object except players.
 *
 * @author craighhann
 *
 */

public interface Entity {

	/**
	 * Returns the name of the entity
	 * @return entity name
	 */
	public String getName();

	/**
	 * Gets location object which describes which
	 * floor an object is on and whereabouts within
	 * that floor they are
	 */

	public Location getLocation();

	/**
	 * Returns the filename for model which
	 * will create the item in the renderer
	 * @return
	 */
	public String getModelName();

	/**
	 * Returns ID assigned to the item
	 * @return
	 */
	public int getID();

}
