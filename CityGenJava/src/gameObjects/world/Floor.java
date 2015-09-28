package gameObjects.world;

import gameObjects.objects.Item;
import gameObjects.player.Player;

import java.util.List;

public class Floor {
	
	private List<Character> players;
	private List<Item> items;
	
	
	
	
	/**
	 * HELPER METHODS
	 * --------------
	 * 
	 * The following methods are for adding and removing players
	 * as they enter or leave floors. They are also for adding or
	 * removing items from the floor as they are picked up or dropped
	 * by the players in the area 
	 * 
	 */
	
	public void addPlayer(Character c){
		players.add(c);
	}
	
	public void removePlayer(Character c){
		players.remove(c);
	}
	
	public void addItem(Item i){
		items.add(i);
	}
	
	public void removeItem(Item i){
		items.remove(i);
	}

}
