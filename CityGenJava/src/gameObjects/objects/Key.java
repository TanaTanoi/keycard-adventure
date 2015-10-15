package gameObjects.objects;

import gameObjects.strategies.KeyInteractStrategy;
import gameObjects.world.Location;

/**
 * A key is a type of tool that can be used on other items
 * such as containers and doors to unlock them. It should also be able to relock
 * the item if need be
 *
 * @author craighhann
 *
 */

public class Key extends Tool {


	public Key(String name, String desc, Location loc, String model, int ID, String image) {
		super(name, desc, loc, model, ID, image);
		super.setStrategy(new KeyInteractStrategy(this));
	}

	@Override
	public String toString() {
		return "Key [getName()=" + getName() + ", getDescription()="
				+ getDescription() + ", getModelName()=" + getModelName()
				+ ", getID()=" + getID() + ", getImagePath()=" + getImagePath()
				+ "]";
	}

}
