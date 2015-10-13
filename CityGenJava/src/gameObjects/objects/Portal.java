package gameObjects.objects;

import gameObjects.world.Location;

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

}
