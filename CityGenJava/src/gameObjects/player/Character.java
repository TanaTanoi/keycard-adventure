package gameObjects.player;

import gameObjects.objects.Item;

public interface Character {

	/**
	 * Returns the character's name
	 * @return name
	 */
	public String getName();
	
	/**
	 * The current character talks to 
	 * another character. Talking may result in a item
	 * being given to the character
	 * 
	 * @return an item
	 */
	public Item talk(Character c);
	
	/**
	 * Moves the character through the game world
	 * @param x is the x direction
	 * @param z is the z direction
	 */
	public void move(int x, int z);
}
