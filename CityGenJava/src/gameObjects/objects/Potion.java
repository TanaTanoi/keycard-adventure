package gameObjects.objects;

import gameObjects.strategies.PotionStrategy;
import gameObjects.world.Location;

public class Potion extends Tool{

	public Potion(String name, String desc, Location loc, int effect, String model, int ID, String image) {
		super(name, desc , loc, model, ID, image);
		super.setStrategy(new PotionStrategy(effect));
	}

}
