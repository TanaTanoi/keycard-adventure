package gameObjects.world;

import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Tool;
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

	public static final int MAX_PLAYERS = 4;

	// For testing

	private char[][] fakeFloor;

	private Map<Integer,Player> allPlayers;
	private List<NPC> allNPCs;
	private Map<Integer, Floor> floorList;
	private int playerID = 0;
	private int itemID = 0;

	public GameWorld(String filename){
		init();
		//Parser is called on ClientController now, so we have more control over when parsing happens
	}

	/**
	 * Initialises world variables
	 */
	public void init(){
		allPlayers = new HashMap<Integer,Player>();
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
			Player p = allPlayers.get(id);//getPlayer(id);
			p.move(x, y);
			p.setOrientation(rotation);
		}
	}

	/**
	 * Adds a new player with the given name, to the game world.
	 * It then returns an ID number of the added player.
	 * If the total number of players (indicated by MAX_PLAYERS) is exceeded,
	 * returns -1.
	 * If the game has already started, and no new players can join, returns -2.
	 * The returned ID is global (accross server and clients).
	 * @param name - Name of player to be added.
	 * @return - The ID of the newly added player; -1 if max players has been reached; -2 if game has already started.
	 */
	public int addNewPlayer(String name){
		if(allPlayers.size()==MAX_PLAYERS)return-1;
		//TODO add condition for if the game has already started.
		Player p = new Player(name, playerID++);
		allPlayers.put(p.getID(),p); // adds player to game world
		// Now adds player to correct floor
		int floor = p.getLocation().getFloor();
		//floorList.get(floor).addPlayer(p);
		return playerID-1;
	}

	/**
	 * Removes the given player from the game
	 * @param playerID - Player to remove
	 */
	public void removePlayer(int playerID){
		Player p = allPlayers.get(playerID);
		//TODO remove player properly, from other things.
		allPlayers.remove(playerID);

	}


	// ADD SEVERAL ITEM CONSTRUCTORS HERE
	// i.e. tool, container etc

	// Actaully make this
	/**
	 * Given a collision map, level int, and a list of items, it creates a floor
	 * and assigns these values to it. Then adds it to the map of floors, under the given
	 * level int.
	 * @param floor - Char array collision map
	 * @param level - unique ID of the floor
	 * @param items - List of items to add to the floor, including furniture.
	 */
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
	 * Finds the closest item to a player that is also within the
	 * given radius.
	 * This method is used for selection of items.
	 * @param l - Center of the radius
	 * @param radius - Max search distance
	 * @return - The closest item to a player within the radius. Null if no objects within the radius
	 */
	public Item closestItem(Location l, float radius){
		Item toReturn = null;
		for(Item i:floorList.get(1).getItems()){//TODO allow multiple floors
			Location iloc = i.getLocation();
			if(iloc.distance(l)<radius){
				if(toReturn == null){
					toReturn = i;
					continue;
				}else if(toReturn.getLocation().distance(l)>iloc.distance(l)){
					//if next object is closer
					toReturn = i;
				}

			}
		}
		return toReturn;
	}



	/**
	 * Adds an item given by a certain item ID to a player specified  by an ID
	 * (Delegates work to PickUpItem(Player,Item) method.
	 * @throws - IllegalArgumentException if the player ID or item ID is not available
	 * @param playerID
	 * @param itemID
	 */
	public void pickUpItem(int playerID, int itemID){
		Player p = allPlayers.get(playerID);
		if(p==null){throw new IllegalArgumentException("Invalid Player ID");}
		pickUpItem(p,itemID);//TODO implement a get Item from ID method
	}

	/**
	 * Picks up an item for a player.
	 * @param p - Player to receive the item
	 * @param i - ID of item to receive
	 */
	public void pickUpItem(Player p, int itemID){
		Floor f = floorList.get(p.getLocation().getFloor());
		Item i = f.getItem(itemID);
		System.out.println("Attemps to pick up item");
		if(p.pickUp(i)){
			System.out.println("Is picking item up");
			f.removeItem(i);
			i.setLocation(null);
		}
	}

	/**
	 * Drops an item from a player at the player's current location
	 * @param p - Player to receive the item
	 * @param i - Item to give to player p
	 */
	public void dropItem(Player p, Item i){
		p.drop(i);
		i.setLocation(p.getLocation());
		Floor f = floorList.get(i.getLocation().getFloor());
		f.addItem(i);
	}

	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<Player>();
		players.addAll(allPlayers.values());
		return players;
	}
	/**
	 * Gets all of the player information and returns them in the following format:
	 * ID X Y ROTATION
	 * ID and Rotation can safely be casted to ints.
	 * @return - List of Player information arrays
	 */
	public List<float[]> getPlayerInfos(){
		List<float[]> toReturn = new ArrayList<float[]>();
		for(Player p:allPlayers.values()){
			Location loc = p.getLocation();
			toReturn.add( new float[]{p.getID(),loc.getX(),loc.getY(),p.getOrientation()});
		}
		return toReturn;
	}
	/**
	 * Add a player to the game
	 * @param p - Player object
	 */
	public void addPlayer(Player p){
		allPlayers.put(p.getID(),p);
		System.out.println("Added player " + allPlayers.size());
	}

	/**
	 * Add a floor to the game
	 * @param f - Floor object
	 */
	public void addFloor(Floor f){
		floorList.put(f.getLevel(), f);
	}

	/**
	 * Sets the ID of a number and increments the total item IDs
	 * to keep it unique.
	 * @return - next unique ID to add.
	 */
	public int setItemID(){
		return itemID++;
	}

	/**
	 * An interaction between a player and an item.
	 * This handles different types of items as well. 			<br>
	 * If pickup-able item - Item is added to player inventory	<br>
	 * If door item - Door is unlocked, if given certain parameters<br>
	 * @param playerID - ID of the player who is interacting
	 * @param itemID - ID of the item that the player has interacted with
	 * @return - True if the interaction was sucesful, false if not.
	 */
	public boolean interact(int playerID,int itemID){
		System.out.println("Picking up item");
		Player p = allPlayers.get(playerID);
		Item i = floorList.get(1).getItem(itemID);//TODO change to be dynamic

		if(i instanceof Key){//TODO change to be door

		}else if(i instanceof Tool){

			pickUpItem(playerID,itemID);
			return true;
		}

		return false;
	}


}







