package gameObjects.objects;

import gameObjects.world.Location;

/**
 * A portal represents a link between two floors in the gameworld. It only appears on the start floor
 * of its location, also indcated by its startFloor ID. It will spawn the player at the location of the portal
 * (in x y) but on the end floor
 *
 * @author craighhann
 *
 */

public class Portal implements Entity {

	int startFloor;
	int endFloor;
	Location loc;
	String modelName;
	int ID;

	public Portal(int startFloor, int endFloor, Location loc, String modelName, int ID) {
		this.startFloor = startFloor;
		this.endFloor = endFloor;
		this.loc = loc;
		this.modelName = modelName;
		this.ID = ID;
	}

	public int getStartFloor() {
		return startFloor;
	}

	public int getEndFloor() {
		return endFloor;
	}

	public Location getLoc() {
		return loc;
	}

	public String getModelName() {
		return modelName;
	}

	@Override
	public String getName() {
		return "Generic Portal";
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public String toString() {
		return "Portal [startFloor=" + startFloor + ", endFloor=" + endFloor
				+ ", modelName=" + modelName + ", ID=" + ID + "]";
	}

}
