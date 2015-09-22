package gameObjects;

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
					//NOTE: Maybe at this point text/ animation needs to appear?
				}
			}
		}

	}

	@Override
	public void useOnSelf(Player p) {
		// Does nothing
	}

}
