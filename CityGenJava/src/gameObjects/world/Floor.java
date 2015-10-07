package gameObjects.world;

import gameObjects.objects.Item;
import gameObjects.player.Character;

import java.util.List;

public class Floor {

	private List<Character> players;
	private List<Item> items;
	private int level;
	private char[][] floor;
	
	public Floor(int level, char[][] floorPlan){
		this.level = level;
		floor = floorPlan;
	}

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
	public int getLevel(){
		return level;
	}	
	
	public char[][] getFloorPlan(){
		return floor;
	}

	public List<Item> getItems(){
		return items;
	}

	public List<Character> getPlayers(){
		return players;
	}

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
