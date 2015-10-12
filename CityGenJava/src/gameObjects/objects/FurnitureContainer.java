package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.world.Location;

/**
 * A funiture container is a type of container that can store any kind of item.
 * They are large items of furniture such as tables, bookshelves, desks etc
 * They are considered a type of container as opposed to furniture only
 * because they are able to 'hold' other items
 *
 * This type of container does not have a key
 *
 * @author Hannah
 *
 */

public class FurnitureContainer extends Container{


	public FurnitureContainer(String name, String desc, int limit, Location loc, String model, int ID) {
		super(name, desc, limit, loc, model,ID);
	}

	@Override
	public void interact(Character c) {
		// Does nothing
	}

	@Override
	public String getModelName() {
		return super.getModelName();
	}

	@Override
	public void addItem(Item i) {
		if(!(i instanceof Furniture)){
			add(i);
		}

	}

}
