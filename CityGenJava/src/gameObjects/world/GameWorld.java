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
	 * @param p
	 * @param i
	 */
	public void pickUpItem(Player p, int itemID){
		Floor f = floorList.get(p.getLocation().getFloor());
		Item i = f.getItem(itemID);
		if(p.pickUp(i)){
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
		return (List<Player>) allPlayers.values();
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

	public void addPlayer(Player p){
		allPlayers.put(p.getID(),p);
		System.out.println("Added player " + allPlayers.size());
	}

	public void addFloor(Floor f){
		floorList.put(f.getLevel(), f);
	}

	public int setItemID(){
		return itemID++;
	}
	/**
	 * Helper method that gets the player from a given Player ID
	 * @param playerID - Global ID of player
	 * @return - Player associated with parameter: playerID or null if player isn't present.
	 */
//	private Player getPlayer(int playerID){
//		for(Player p:allPlayers){
//			if(p.getID()==playerID){
//				return p;
//			}
//		}
//		return null;
//	}
}







