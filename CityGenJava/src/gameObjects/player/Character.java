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
	
	/**
	 * Checks whether the character is alive
	 * i.e. whether their health is above zero
	 * @return living status
	 */
	
	public boolean isAlive();
	
	/**
	 * Attack allows a character to attack another character
	 * and do a given amount of damage to their health
	 * 
	 * @param damage is the amount of health the NPC loses
	 */
	public void attack(int damage);
		
}
