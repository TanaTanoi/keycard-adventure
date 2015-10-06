package gameObjects.objects;
import javafx.geometry.BoundingBox;
import gameObjects.strategies.WeaponStrategy;
import gameObjects.world.Location;

public class Weapon extends Tool {

	public Weapon(String name, String desc, Location loc, int damage, String model, int ID) {
		super(name, desc, loc, model, ID);
		super.setStrategy(new WeaponStrategy(damage));
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
