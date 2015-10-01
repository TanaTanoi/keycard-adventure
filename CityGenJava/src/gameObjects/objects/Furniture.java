package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.world.Location;
import javafx.geometry.BoundingBox;

public class Furniture implements Item {

	private String name;
	private String description;
	private Location loc;

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getDescription(){
		return description;
	}

	@Override
	public void interact(Character c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interact(Item i) {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation() {
		return loc;
	}

}
