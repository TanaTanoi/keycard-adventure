package gameObjects.strategies;

/**
 * A map can only be picked, therefore it should have no interactions
 */

import gameObjects.objects.Item;
import gameObjects.player.Character;

public class MapStrategy implements InteractStrategy{

	@Override
	public void interact(Item i) {
		// Does nothing
	}

	@Override
	public void useOnPlayer(Character c) {
		// Does nothing
	}

}
