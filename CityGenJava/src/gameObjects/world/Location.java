package gameObjects.world;

public class Location {
	
	private int floor;
	private int x;
	private int y;
	
	public Location(int x, int y, int floor){
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
