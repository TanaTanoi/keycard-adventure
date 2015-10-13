package gameObjects.objects;


import gameObjects.strategies.KeyInteractStrategy;
import gameObjects.world.Location;

public class Key extends Tool {


	public Key(String name, String desc, Location loc, String model, int ID, String image) {
		super(name, desc, loc, model, ID, image);
		super.setStrategy(new KeyInteractStrategy(this));
	}

}
