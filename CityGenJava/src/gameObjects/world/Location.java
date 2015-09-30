package gameObjects.world;

public class Location {

	private int floor;
	private float x;
	private float y;

	public Location(float x, float y, int floor){
		this.x = x;
		this.y = y;
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
