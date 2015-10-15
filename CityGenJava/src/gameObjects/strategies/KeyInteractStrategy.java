package gameObjects.strategies;
import gameObjects.objects.Key;
import gameObjects.objects.Item;
import gameObjects.objects.Container;
import gameObjects.player.Character;

/**
 * The key interact strategy describes how key objects may
 * interact with the user and other items in the game world
 *
 * @author Hannah
 *
 */

public class KeyInteractStrategy implements InteractStrategy {

	private Key key;

	public KeyInteractStrategy(Key k){
		this.key = k;
	}

	@Override
	public void interact(Item i) {
		// If a container has been clicked
		if(i instanceof Container){
			Container cont = (Container) i;
			// If the container has a lock
			if(cont.hasKey()){
				// If this key unlocks/locks the container
				if(key.getName() == cont.getKeyName()){
					// Unlock/lock the container
					cont.setLockStatus(key);
				}
			}
		}


	}

	@Override
	public void useOnPlayer(Character c) {
		// Does nothing
	}

}
