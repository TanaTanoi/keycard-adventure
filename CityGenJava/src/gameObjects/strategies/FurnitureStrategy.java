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
public interface FurnitureStrategy {
	
	/**
	 * Describes how an item interacts
	 * with the piece if furniture
	 *  
	 * @param i is the other item
	 */
	public void interact(Item i);
	
	/**
	 * Describes how a player can 
	 * interact with this piece of furniture
	 *  
	 * @param p is the player using this item
	 */
	public void usedByPlayer(Character c);
		
}