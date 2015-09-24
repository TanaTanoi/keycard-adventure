package gameObjects.objects;

import javafx.geometry.BoundingBox;
import gameObjects.player.Character;

/**
 * The item interface describes objects in the game world that the player can interact with
 * Note that there are two interact methods an item may have, either to perform an action on
 * other objects or on a player. This may not be need for all items.
 * 
 * @author Hannah
 *
 */

public interface Item {
	
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
	 * Returns the name of the item
	 * @return item name
	 */
	public String getName();
	
	/**
	 * Returns a description about the item
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Gets bounding box of item.
	 * This can then be used for collision detection
	 * or for selecting items
	 */
	public BoundingBox getBoundingBox();
}
