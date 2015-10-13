package gameObjects.objects;
import gameObjects.strategies.WeaponStrategy;
import gameObjects.world.Location;

public class Weapon extends Tool {

	public Weapon(String name, String desc, Location loc, int damage, String model, int ID, String image) {
		super(name, desc, loc, model, ID, image);
		super.setStrategy(new WeaponStrategy(damage));
	}

}
