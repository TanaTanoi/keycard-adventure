package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.world.Location;

/**
 * A furniture item represents an item that's only purpose is to decorate the
 * room or create some kind of non movable collision
 *
 * You cannot interact with them or move them
 * @author craighhann
 *
 */

public class Furniture implements Item {

	private int ID;
	private Location loc;
	private String model;

	public Furniture(Location loc, String model, int ID){
		this.loc = loc;
		this.model = model;
		this.ID = ID;
	}

	@Override
	public String getName(){
		return "";
	}

	@Override
	public String getDescription(){
		return "";
	}

	@Override
	public void interact(Character c) {
		// Cannot interact with player

	}

	@Override
	public void interact(Item i) {
		// Cannot interact with item
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public void setLocation(Location l) {
		loc = l;
	}

	@Override
	public String getModelName() {
		return model;
	}

	@Override
	public int getID() {
		return ID;
	}

}
