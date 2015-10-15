package gameObjects.world;

/**
 * Represents where an entity is in the world. It has an x and y value for where the entity
 * is on the floor they are on. It also has an integer to represent what level floor
 * the entity is on
 *
 * @author craighhann
 *
 */

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

	/**
	 * The distance between this and another location
	 * @param other - Other location to get the distance from
	 * @return- Length of space between the locations
	 */
	public float distance(Location other){
		return (float) Math.hypot(x-other.getX(),y-other.getY());
	}

}
