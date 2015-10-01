package gameObjects.objects;

import javafx.geometry.BoundingBox;
import gameObjects.strategies.KeyInteractStrategy;
import gameObjects.world.Location;

public class Key extends Tool {


	public Key(String name, String desc, Location loc, String model) {
		super(name, desc, loc, model);
		super.setStrategy(new KeyInteractStrategy(this));
	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
