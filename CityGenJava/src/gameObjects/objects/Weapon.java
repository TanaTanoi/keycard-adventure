package gameObjects.objects;
import gameObjects.strategies.WeaponStrategy;
import gameObjects.world.Location;

/**
 * A weapon is a type of tool that players can use on other players
 * to decrease their health. The strategy pattern it uses determines
 * how much it will affect the other player
 *
 * @author craighhann
 *
 */

public class Weapon extends Tool {

	public Weapon(String name, String desc, Location loc, int damage, String model, int ID, String image) {
		super(name, desc, loc, model, ID, image);
		super.setStrategy(new WeaponStrategy(damage));
	}

}
