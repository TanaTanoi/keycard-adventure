package gameObjects.player;

import gameObjects.objects.Item;
import gameObjects.world.Location;

/**
 * The player class describes a character in the game
 * that is controlled by a user 
 * 
 * @author Hannah
 *
 */

public class Player implements Character{
	
	private String name; // name selected by user
	private Item [] inventory; // items  that the player carries 
	private int health;
	private Location loc; // describes player's location in game world	
	
	public Player(String name){
		this.name = name;
	}
	
	/**
	 * Moves the player's position in the game world
	 * @param x is the x direction to move the player
	 * @param z is the z direction to move the player
	 */
	
	public void move(int x, int z){
		loc.setX(x);
		loc.setY(z);
	}
	
	@Override
	public Item talk(Character c){
		return null;
	}

	@Override
	public String getName() {
		return name;		
	}
	
	/**
	 * Taking a potion has an effect on the character's health
	 * This can be either a positive or a negative effect
	 * @param effect
	 */
	public void takePotion(int effect){
		health += effect;
	}
	
	@Override
	public void attack(int damage){
		health -= damage;
	}

	@Override
	public boolean isAlive() {
		return health > 0;
	}

	@Override
	public Location getLocation() {
		return loc;
	}
	
}
