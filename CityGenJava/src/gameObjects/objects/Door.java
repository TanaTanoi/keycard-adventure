package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.player.Player;
import gameObjects.world.Location;

/**
 * A door is a type of object that blocks players from entering a room.
 * While we only currently use doors while they are locked, it is possible to extend this in the future
 * @author craighhann *
 */

public class Door implements Item{
	String name;
	String description;
	Location loc;
	boolean isLocked;
	String keyName;
	String modelName;
	int ID;

	public Door(String name, String description, Location loc,
			boolean isLocked, String keyName, String modelName, int ID) {
		this.name = name;
		this.description = description;
		this.loc = loc;
		this.isLocked = isLocked;
		this.keyName = keyName;
		this.modelName = modelName;
	}

	@Override
	public void interact(Character c) {
		Player p = (Player)c;

		if(p.useKey(keyName)){

			isLocked = false;
		}
	}

	@Override
	public void interact(Item i) {
		if(i instanceof Key){
			Key k = (Key)i;
			if(k.getName().equals(keyName)){
				isLocked = false;
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public void setLocation(Location l) {
		// should not be able to reset location of door
	}

	@Override
	public String getModelName() {
		return modelName;
	}

	@Override
	public int getID() {
		return ID;
	}

	public boolean isLocked(){
		return isLocked;
	}

}
