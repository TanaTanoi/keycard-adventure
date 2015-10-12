package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.world.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * The container class describes an item that can hold other items
 * inside of it. It is possible to have containers that can contain
 * infinite amounts of items (-1 limit) or a limit can be specified in the constructor.
 *
 * The container may only be able to take items of a certain size or type
 *
 * The container also may be locked and may be only opened with a specific
 * type of key
 *
 * @author Hannah
 *
 */

public abstract class Container implements Item{

	private int ID;

	// Fields describing the container
	private String name;
	private String desc;
	private int limit;
	private Location loc;
	private String modelName;
	private boolean isLocked;

	// All the items the container has
	private List<Item> inventory;

	// Key for the container (optional)
	private Key key;

	/**
	 * Constructor for a container that does not have
	 * a lock and therefore has no need for a key
	 *
	 * @param name is the name of the container
	 * @param desc describes the container
	 * @param limit is how many items the container may have
	 */
	public Container(String name, String desc, int limit, Location loc, String model, int ID){
		init(name,desc,limit,loc,model,ID);
	}

	/**
	 * Constructor for a container that has a key
	 * and may start locked or unlocked
	 *
	 * @param name is the name of the container
	 * @param desc describes the container
	 * @param limit is how many items the container may have
	 * @param locked determines whether the container is locked
	 * @param k is the key that can open/lock a container
	 */
	public Container(String name, String desc, int limit, boolean locked, Key k, Location loc, String model, int ID){
		init(name,desc,limit,locked,k,loc, model, ID);
	}

	/**
	 * Initialises a container that has no key
	 *
	 * @param name
	 * @param desc
	 * @param limit
	 */
	public void init(String name, String desc, int limit, Location loc, String model, int ID){
		this.name = name;
		this.desc = desc;
		this.limit = limit;
		this.loc = loc;
		this.ID = ID;
		modelName = model;
		inventory = new ArrayList<Item>();
	}

	/**
	 * Initialises a container with a key
	 *
	 * @param name
	 * @param desc
	 * @param limit
	 * @param locked
	 * @param k
	 */
	public void init(String name, String desc, int limit, boolean locked, Key k, Location loc, String model, int ID){
		init(name,desc,limit, loc, model, ID);
		isLocked = locked;
		key = k;
	}

	@Override
	public void interact(Item i) {
		if(i instanceof Key){
			setLockStatus((Key)i);
		}

	}

	/**
	 * Unlocks/lock the container if the key is correct
	 * @param k is the key tried
	 */
	public void setLockStatus(Key k){
		if(k.getName().equals(key.getName())){
			isLocked = !isLocked;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return desc;
	}

	/**
	 * Determines whether the container has a key
	 * @return boolean indicating whether the container has a key
	 */
	public boolean hasKey(){
		return key != null;
	}

	/**
	 * Returns name of the key that locks/unlocks it
	 * @return
	 */
	public String getKeyName(){
		return key.getName();
	}

	@Override
	public void setLocation(Location loc){
		this.loc = loc;
	}

	@Override
	public Location getLocation(){
		return loc;
	}

	@Override
	public String getModelName(){
		return modelName;
	}

	@Override
	public int getID(){
		return ID;
	}

	public abstract void addItem(Item i);

	public void add(Item i){
		inventory.add(i);
	}

}
