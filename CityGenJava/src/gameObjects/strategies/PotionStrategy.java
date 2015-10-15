package gameObjects.strategies;
import gameObjects.player.Character;
import gameObjects.player.Player;
import gameObjects.objects.Item;

public class PotionStrategy implements InteractStrategy {

	private int healthChange;

	public PotionStrategy(int effect) {
		healthChange = effect;
	}

	@Override
	public void interact(Item i) {
		// Allow put in container

	}

	@Override
	public void useOnPlayer(Character c) {
		Player p = (Player)c;
		p.takePotion(healthChange);
	}

}
