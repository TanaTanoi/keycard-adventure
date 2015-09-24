package gameObjects.world;

import gameObjects.objects.Item;
import gameObjects.player.NPC;
import gameObjects.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class GameWorld {
	
	private List<Item> allItems;
	private List<Player> allPlayers;
	private List<NPC> allNPCs;
	
	public GameWorld(String filename){
		parseWorld(filename);
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

}
