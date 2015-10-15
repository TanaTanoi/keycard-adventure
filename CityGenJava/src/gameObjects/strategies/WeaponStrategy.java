package gameObjects.strategies;
import gameObjects.player.Character;
import gameObjects.objects.Item;

public class WeaponStrategy implements InteractStrategy {

	private int damage;

	public WeaponStrategy(int d){
		damage = d;
	}


	@Override
	public void interact(Item i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void useOnPlayer(Character c) {
		if(c != null){
			c.attack(damage);
		}
	}

}
