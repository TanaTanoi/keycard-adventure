package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.world.Location;
import javafx.geometry.BoundingBox;

public class Furniture implements Item {

	private int ID;

	private String name;
	private String description;
	private Location loc;
	private String model;

	public Furniture(String name, String desc, Location loc, String model, int ID){
		this.name = name;
		description = desc;
		this.loc = loc;
		this.model = model;
		this.ID = ID;
	}

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
