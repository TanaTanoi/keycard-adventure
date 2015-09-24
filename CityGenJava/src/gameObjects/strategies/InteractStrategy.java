package gameObjects.strategies;
import gameObjects.objects.Item;
import gameObjects.player.Character;

/**
 * The interact strategy interface is used to implement a strategy pattern
 * for the tool objects. The interact strategy determines how a tool
 * can interact or be applied to oneself
 * 
 * @author Hannah
 *
 */
public interface InteractStrategy {
	
	/**
	 * Describes how the item using this strategy may 
	 * interact with another object in the game world
	 * 
	 * @param i is the other item
	 */
	public void interact(Item i);
	
	/**
	 * Describes how the item using this strategy may
	 * be used on the player who is using it
	 * 
	 * @param p is the player using this item
	 */
	public void useOnPlayer(Character c);
		
}
