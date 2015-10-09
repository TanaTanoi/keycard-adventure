package gameObjects.world;

import gameObjects.objects.Item;
import gameObjects.player.NPC;
import gameObjects.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameWorld {

	// For testing

	private char[][] fakeFloor;

	private List<Player> allPlayers;
	private List<NPC> allNPCs;
	private Map<Integer, Floor> floorList;
	private int playerID = 0;
	private int itemID = 0;

	public GameWorld(String filename){
		init();
		//Parser is called on ClientController now
	}

	/**
	 * Initialises world variables
	 */
	public void init(){
		allPlayers = new ArrayList<Player>();
		allNPCs = new ArrayList<NPC>();
		floorList = new HashMap<Integer,Floor>();
	}

	/**
	 * Update the player with the provided ID to the new X,Y position, and rotation
	 * @param id -ID of the player who will be changed
	 * @param x - actual x value of the player
	 * @param y - actual y value of the player
	 * @param rotation - direction of the player
	 */
	public void updatePlayerInfo(int id, float x, float y, int rotation){
		if(allPlayers.size()>id){
			//			System.out.println("Updating player " + id + " to " + x + " " +y);
			for(Player p:allPlayers){
				if(p.getID() == id){
					p.move(x, y);
					p.setOrientation(rotation);
				}
			}
		}
	}

	public int addNewPlayer(String name){
		Player p = new Player(name, playerID++);
		allPlayers.add(p); // adds player to game world
		// Now adds player to correct floor
		int floor = p.getLocation().getFloor();
		//floorList.get(floor).addPlayer(p);
		return playerID-1;
	}

	// ADD SEVERAL ITEM CONSTRUCTORS HERE
	// i.e. tool, container etc

	// Actaully make this
	public void setFloor(char[][] floor, int level, List<Item> items){
		fakeFloor = floor;
		Floor f = new Floor(level, floor, items);
		System.out.println("Adding floor at " + level + " with " + items.size() + " items");
		floorList.put(level, f);
	}
	public Floor getFloor(){
		return floorList.get(1);
	}
	public char[][] getCollisions(){
		return fakeFloor;
	}



	/**
	 * Picks up an item for a player.
	 * @param p
	 * @param i
	 */
	public void pickUpItem(Player p, Item i){
		if(p.pickUp(i)){
			Floor f = floorList.get(i.getLocation().getFloor());
			f.removeItem(i);
			i.setLocation(null);
		}
	}

	/**
	 * Drops an item from a player at the player's current location
	 * @param p
	 * @param i
	 */
	public void dropItem(Player p, Item i){
		p.drop(i);
		i.setLocation(p.getLocation());
		Floor f = floorList.get(i.getLocation().getFloor());
		f.addItem(i);
	}

	public List<Player> getPlayers(){
		return allPlayers;
	}
	/**
	 * Gets all of the player information and returns them in the following format:
	 * ID X Y ROTATION
	 * @return - List of Player information arrays
	 */
	public List<float[]> getPlayerInfos(){
		List<float[]> toReturn = new ArrayList<float[]>();
		for(Player p:allPlayers){
			Location loc = p.getLocation();
			toReturn.add( new float[]{p.getID(),loc.getX(),loc.getY(),p.getOrientation()});
		}
		return toReturn;
	}
	public void addPlayer(Player p){
		allPlayers.add(p);
		System.out.println("Added player " + allPlayers.size());
	}

	public void addFloor(Floor f){
		floorList.put(f.getLevel(), f);
	}

	public int setItemID(){
		return itemID++;
	}
}