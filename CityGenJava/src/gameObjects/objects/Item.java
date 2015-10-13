package gameObjects.objects;
import gameObjects.player.Character;
import gameObjects.world.Location;

/**
 * The item interface describes objects in the game world that the player can interact with
 * Note that there are two interact methods an item may have, either to perform an action on
 * other objects or on a player. This may not be need for all items.
 *
 * @author Hannah
 *
 */

public interface Item extends Entity {

	/**
	 * This item performs an action on a player	 *
	 */
	public void interact(Character c);

	/**
	 * Performs action on this item with another item
	 * NOTE: may need a mouse click or object interacting with as param
	 */
	public void interact(Item i);



	/**
	 * Returns a description about the item
	 * @return
	 */
	public String getDescription();



	/**
	 * Sets location of object. It is set to null
	 * if being carried by a player. When it is
	 * dropped by a player, it's new location can be
	 * set again using this
	 * @return
	 */
	public void setLocation(Location l);


}
