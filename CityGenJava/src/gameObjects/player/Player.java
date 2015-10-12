package gameObjects.player;

import java.util.ArrayList;
import java.util.List;

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
	private List<Item> inventory; // items  that the player carries
	private static int MAX_ITEMS = 5;
	private int health = 100;
	private Location loc; // describes player's location in game world
	private int ID;
	private int orientation;


	public Player(String name, int ID){
		this.name = name;
		this.ID = ID;
		loc = new Location(0,0,0);
		inventory = new ArrayList<Item>();
	}

	/**
	 * Moves the player's position in the game world
	 * @param x is the x direction to move the player
	 * @param z is the z direction to move the player
	 */

	public void move(float x, float z){
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

	public int getID(){
		return ID;
	}

	public int hashCode(){
		return getID();
	}

	/**
	 * Picks up an item (if there is enough room)
	 * and adds it to the player's inventory
	 *
	 * @param i is the item
	 * @return a boolean indicating whether the action was succesful
	 */
	public boolean pickUp(Item i){
		if(inventory.size() < MAX_ITEMS){
			inventory.add(i);
			return true;
		}
		System.out.println("No more room!");
		return false;
	}

	/**
	 * Drops an item from a player's inventory
	 * @param i
	 */
	public void drop(Item i){
		inventory.remove(i);
	}

	@Override
	public int getOrientation(){
		return orientation;
	}

	@Override
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	@Override
	public void changeOrientation(int change) {
		orientation += change;

		if(orientation < 0){
			orientation += 360;
		}
		else if(orientation > 359){
			orientation -= 360;
		}
	}
}