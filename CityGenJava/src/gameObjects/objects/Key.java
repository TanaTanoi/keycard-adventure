package gameObjects.objects;


import gameObjects.strategies.KeyInteractStrategy;
import gameObjects.world.Location;

public class Key extends Tool {


	public Key(String name, String desc, Location loc, String model, int ID) {
		super(name, desc, loc, model, ID);
		super.setStrategy(new KeyInteractStrategy(this));
	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub

	}

}
