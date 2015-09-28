package gameObjects.objects;

import javafx.geometry.BoundingBox;
import gameObjects.strategies.PotionStrategy;
import gameObjects.world.Location;

public class Potion extends Tool{

	public Potion(String name, String desc, Location loc, int effect) {
		super(name, desc , loc);
		super.setStrategy(new PotionStrategy(effect));
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
