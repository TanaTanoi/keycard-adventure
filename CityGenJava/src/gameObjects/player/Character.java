package gameObjects.player;

import gameObjects.objects.Item;
import gameObjects.world.Location;

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
	
	/**
	 * Gets a location object that stores data about where the
	 * character is located in the game world. There is an integer
	 * that represents what floor/level 
	 * the character is on. In the GameWorld, there is a 
	 * map of floor levels to floors. 
	 * 
	 * It also has the x and y coordinates of where a character 
	 * is within the floor they are on
	 */
	
	public Location getLocation();	
		
}
