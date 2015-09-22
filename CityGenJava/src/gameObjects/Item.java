package gameObjects;

/**
 * The item interface describes objects in the game world that the player can interact with
 * @author Hannah
 *
 */

public interface Item {
	
	/**
	 * Performs action on/with item
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
}
