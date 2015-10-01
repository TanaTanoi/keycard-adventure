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

	private List<Player> allPlayers;
	private List<NPC> allNPCs;
	private Map<Integer, Floor> floorList;
	private int ID = 0;

	public GameWorld(String filename){
		init();
		//parseWorld(filename);
	}

	/**
	 * Open's the given file and parses
	 * all the information to create the gameworld.
	 *
	 * @param filename is the name of the file to parse
	 */
	public void parseWorld(String filename) {
		File f = new File(filename);
		try {
			Scanner s = new Scanner(f);
			while(s.hasNextLine()){
				// Do actual parsing in here
			}
		} catch (FileNotFoundException e) {e.printStackTrace(); }

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
	 * Update the player with the provided ID to the new X,Y position
	 * @param id
	 * @param x
	 * @param y
	 */
	public void updatePlayerInfo(int id, float x, float y){
		if(allPlayers.size()>id){
//			System.out.println("Updating player " + id + " to " + x + " " +y);
			for(Player p:allPlayers){
				if(p.getID() == id)p.move(x, y);
			}
		}
	}

	public int addNewPlayer(String name){
		Player p = new Player(name, ID++);
		allPlayers.add(p); // adds player to game world
		// Now adds player to correct floor
		int floor = p.getLocation().getFloor();
		//floorList.get(floor).addPlayer(p);
		return ID-1;
	}

	/**
	 * Picks up an item for a player.
	 * @param p
	 * @param i
	 */
	public void pickUpItem(Player p, Item i){
		p.pickUp(i);
		i.setLocation(null);
	}

	/**
	 * Drops an item from a player at the player's current location
	 * @param p
	 * @param i
	 */
	public void dropItem(Player p, Item i){
		p.drop(i);
		i.setLocation(p.getLocation());
	}

	public List<Player> getPlayers(){
		return allPlayers;
	}

	public void addPlayer(Player p){
		allPlayers.add(p);
		System.out.println("Added player " + allPlayers.size());
	}
}