package gameObjects.world;

public class Portal {

	int startFloor;
	int endFloor;
	Location loc;
	String modelName;

	public Portal(int startFloor, int endFloor, Location loc, String modelName) {
		this.startFloor = startFloor;
		this.endFloor = endFloor;
		this.loc = loc;
		this.modelName = modelName;
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

}
