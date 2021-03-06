package gameObjects.player;

import java.util.ArrayList;
import java.util.List;

import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Map;
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
	private boolean canSeeMap;

	public Player(String name, int ID){
		this.name = name;
		this.ID = ID;
		loc = new Location(3,-4,1);
		inventory = new Tool[2];
	}

	/**
	 * Equips the item in the left side of the inventory, if there is one
	 */
	public void equipLeft(){
		if(equipped != 0){ // swap to or equip the left item
			equipped = 0;
		}
		else{ // unequip the left item
			equipped = -1;
		}
	}

	/**
	 * Equips the item in the right side of the inventory, if there is one
	 */
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
			if(i instanceof Map){
				canSeeMap = true;
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
		if(t instanceof Map){
			canSeeMap = false;
		}
		return t;
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

	/**
	 * Gets the health of the player
	 * @return
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the health of the player
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Gets the index of the equipped item
	 * in the inventory, returns -1 if no item equipped
	 * @return index of equipped tool in inventory
	 */
	public int getEquipped() {
		return equipped;
	}

	/**
	 * Returns the tool equipped by the player
	 * and null if no item equipped
	 * @return equipped tool
	 */
	public Tool getEquippedTool(){
		if(getEquipped() == -1){
			return null;
		}
		return inventory[getEquipped()];
	}

	/**
	 * Sets player's floor
	 * @param f
	 */
	public void setFloor(int f){
		loc.setFloor(f);
	}

	/**
	 * Returns player inventory
	 * @return
	 */
	public Tool[] getInventory() {
		return inventory;
	}

	/**
	 * Indicates whether the player can see a map. This should
	 * only be enabled if they have a map item in their inventory.
	 * If so, they will see a minimap on their dashboard
	 *
	 * @return
	 */
	public boolean canSeeMap(){
		return canSeeMap;
	}

	/**
	 * Lets a player use an item on themselves.
	 * Currently, this is only relevant for potions
	 * which players can take to affect their health
	 *
	 * @param itemID
	 */
	public void useItem(int itemID) {
		if(inventory[0]!=null&&inventory[0].getID()==itemID){
			inventory[0] .interact(this);
			inventory[0] = null;
			noItems--;
		}else if(inventory[1]!=null&&inventory[1].getID()==itemID){
			inventory[1] .interact(this);
			inventory[1] = null;
			noItems--;
		}
	}

	/**
	 * This method is  called by the game world to check whether a player
	 * has the correct key in their inventory to unlock a locked entity
	 * they have clicked on. It returns true if they have a key with the
	 * name passed in.
	 *
	 * @param keyName - The name of the key expected to unlock the door this has been used on.
	 * @return
	 */
	public boolean useKey(String keyName) {
		if(inventory[0]!=null&&inventory[0] instanceof Key){
			if(((Key)inventory[0]).getName().equals(keyName)){
				inventory[0] = null;
				noItems--;
				return true;
			}
		}else if(inventory[1]!=null&&inventory[1] instanceof Key){
			if(((Key)inventory[1]).getName().equals(keyName)){
				inventory[1] = null;
				noItems--;
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets location
	 * @param loc
	 */
	public void setLocation(Location loc){
		this.loc = loc;
	}
}