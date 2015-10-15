package gameObjects.strategies;
import gameObjects.player.Character;
import gameObjects.objects.Item;

/**
 * Details how a weapon should be used.
 * If used on a player, the damage it can do should be applied to the player
 *
 * @author craighhann
 */

public class WeaponStrategy implements InteractStrategy {

	private int damage;

	public WeaponStrategy(int d){
		damage = d;
	}


	@Override
	public void interact(Item i) {
		// Do nothing
	}

	@Override
	public void useOnPlayer(Character c) {
		if(c != null){
			c.attack(damage);
		}
	}

}
