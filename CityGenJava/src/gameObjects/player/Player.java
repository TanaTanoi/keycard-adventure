package gameObjects.player;

import java.util.ArrayList;
import java.util.List;

import gameObjects.objects.Item;
import gameObjects.objects.Potion;
import gameObjects.objects.Tool;
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
	private Tool[] inventory; // items  that the player carries
	private int MAX_HEALTH = 100;
	private int health = 100;
	private int noItems = 0;
	private Location loc; // describes player's location in game world
	private int ID;
	private int orientation;
	private int equipped = -1;

	public Player(String name, int ID){
		this.name = name;
		this.ID = ID;
		loc = new Location(3,-4,1);
		inventory = new Tool[2];
	}

	public void equipLeft(){
		if(equipped != 0){ // swap to or equip the left item
			equipped = 0;
		}
		else{ // unequip the left item
			equipped = -1;
		}
	}

	public void equipRight(){
		if(equipped != 1){ // swap to or equip the right item
			equipped = 1;
		}
		else{ // unequip the right item
			equipped = -1;
		}
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
		if(health > MAX_HEALTH){
			health = MAX_HEALTH;
		}
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
	public boolean pickUp(Tool i){
		if(noItems < inventory.length){
			// adds to left slot first
			if(inventory[0] == null){
				inventory[0] = i;
			}
			// adds to right slot second
			else{
				inventory[1] = i;
			}
			noItems++;
			return true;
		}
		System.out.println("No more room!");
		return false;
	}

	/**
	 * Drops an item from a player's inventory
	 * @param i
	 */
	public Tool drop(int inventoryID){
		Tool t =  inventory[inventoryID];
		inventory[inventoryID] = null;
		noItems--;
		return t;


//		if(equipped != -1){
//			Tool t = inventory[equipped];
//			inventory[equipped] = null;
//			noItems--;
//			return t;
//		}
//		return null;
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}


	public int getEquipped() {
		return equipped;
	}

	public Tool getEquippedTool(){
		if(getEquipped() == -1){
			return null;
		}
		return inventory[getEquipped()];
	}

	public void setFloor(int f){
		loc.setFloor(f);
	}

	public Tool[] getInventory() {
		return inventory;
	}

	public void useItemOnSelf() {
		if(equipped != -1){ // has item equipped
			inventory[equipped].interact(this);
			inventory[equipped] = null;
		}
	}
}