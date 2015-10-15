package gameObjects.world;

import gameObjects.objects.Container;
import gameObjects.objects.Door;
import gameObjects.objects.Entity;
import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Portal;
import gameObjects.objects.Tool;
import gameObjects.objects.Weapon;
import gameObjects.player.AttackNPC;
import gameObjects.player.InfoNPC;
import gameObjects.player.NPC;
import gameObjects.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.omg.CORBA.Current;

import vec.Vector2;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

/**
 *
 * The GameWorld represents all the state in the game. It stores a list of all the floors in the game and all players in the game.
 * It also has game logic for determing whether players should be able to interact with objects and determining how they should.
 * There is a main copy of the gameWorld held on the server. Any game state changes made there are updated to all client versions
 * of the gameworld. Events that only affect the client, such as an NPC displaying text, only happen in the client's gameworld
 *
 * @author craighhann
 *
 */

public class GameWorld {

	public static final int MAX_PLAYERS = 3;

	private Map<Integer,Player> allPlayers;
	private Map<Integer, Floor> floorList;
	private int TOTAL_PLAYER_COUNT = 0; // used as counter for unique player ID's
	private int TOTAL_ENTITY_COUNT = 0; // used as counter for unique entity ID's

	public GameWorld(){
		init();
	}

	/**
	 * Initialises world variables
	 */
	public void init(){
		allPlayers = new HashMap<Integer,Player>();
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
			Player p = allPlayers.get(id);
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
	 * The returned ID is global (across server and clients).
	 * @param name - Name of player to be added.
	 * @return - The ID of the newly added player; -1 if max players has been reached; -2 if game has already started.
	 */
	public int addNewPlayer(String name){
		if(allPlayers.size()==MAX_PLAYERS)return-1;
		Player p = new Player(name, TOTAL_PLAYER_COUNT++);
		allPlayers.put(p.getID(),p); // adds player to game world
		// Now adds player to correct floor
		int floor = p.getLocation().getFloor();
		return TOTAL_PLAYER_COUNT-1;
	}

	/**
	 * Removes the given player from the game
	 * @param playerID - Player to remove
	 */
	public void removePlayer(int playerID){
		Player p = allPlayers.get(playerID);
		allPlayers.remove(playerID);
	}

	/**
	 * Given a collision map, level int, and a list of items, it creates a floor
	 * and assigns these values to it. Then adds it to the map of floors, under the given
	 * level int.
	 * @param floor - Char array collision map
	 * @param level - unique ID of the floor
	 * @param items - List of items to add to the floor, including furniture.
	 */
	public void setFloor(char[][] floor, int level, List<Entity> items){
		Floor f = new Floor(level, floor, items);
		System.out.println("Adding floor at " + level + " with " + items.size() + " items");
		floorList.put(level, f);
	}

	/**
	 * Returns the correct floor
	 * when given a level
	 *
	 * @param floor
	 * @return
	 */
	public Floor getFloor(int floor){
		return floorList.get(floor);
	}


	/**
	 * Finds the closest item to a player that is also within the
	 * given radius.
	 * This method is used for selection of items.
	 * @param l - Center of the radius
	 * @param radius - Max search distance
	 * @param rot
	 * @return - The closest item to a player within the radius. Null if no objects within the radius
	 */

	public Entity closestEntity(Location l, float radius,int rot){
		Entity toReturn = null;

		for(Entity i: floorList.get(l.getFloor()).getEntities()){
			Location iloc = i.getLocation();

			Vector2 ent_to_pl = new Vector2(iloc.getX()-l.getX(),iloc.getY()-l.getY());
			ent_to_pl = ent_to_pl.unitVector();
			Vector2 p_dir = new Vector2(
					(float)-Math.sin(Math.toRadians(rot)),
					(float)Math.cos(Math.toRadians(rot)));
			p_dir = p_dir.unitVector();
			float anglediff = p_dir.dot(ent_to_pl.mul(1.0f));

			if(iloc.distance(l)<radius&&Math.round(anglediff*10.0f)/10.0f==1){

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
	public boolean pickUpItem(int playerID, int itemID){
		Player p = allPlayers.get(playerID);
		if(p==null){throw new IllegalArgumentException("Invalid Player ID");}

		Floor f = floorList.get(p.getLocation().getFloor());
		Entity i = f.getEntity(itemID);

		if(!(i instanceof Tool)){
			return false;
		}
		Tool t = (Tool)i;
		if(p.pickUp(t)){
			f.removeEntity(t);
			t.setLocation(null);
			return true;
		}

		return false;
	}

	/**
	 * Changes the players location to the new floor
	 * and portal x,y position
	 *
	 * @param playerID
	 * @param portalID
	 * @return
	 */
	public void moveFloor(int playerID, int portalID){
		Player p = allPlayers.get(playerID);
		if(p==null){throw new IllegalArgumentException("Invalid Player ID");}

		Floor f = floorList.get(p.getLocation().getFloor());
		Portal i = (Portal)f.getEntity(portalID);

		if(i == null){System.out.println("Portal is null"); System.out.println("Floor " + f.getLevel());}

		p.move(i.getLocation().getX()+1, i.getLocation().getY()+1); // moves to portal location
		p.setFloor(i.getEndFloor()); // updates players floor
		f.removePlayer(p); // removes from current floor
		// adds to new floor
	}

	/**
	 * Drops an item from a player at the player's current location
	 * @param p - Player to receive the item
	 * @param i - Item to give to player p
	 */
	public boolean dropItem(Player p, int itemID){
		System.out.println("Dropping from player");
		Tool[] tools = p.getInventory();
		Item i;
		if(tools[0]!=null&&tools[0].getID() == itemID){
			i = p.drop(0);
		}else if(tools[1]!=null&&tools[1].getID() == itemID){
			i = p.drop(1);
		}else{
			return false;
		}
		Location dropLoc = 	new Location(p.getLocation().getX() + 1, p.getLocation().getY(), p.getLocation().getFloor());
		i.setLocation(dropLoc);
		Floor f = floorList.get(i.getLocation().getFloor());
		f.addEntity(i);
		return true;
	}

	/**
	 * Gets a list of all players currently connected and in the game
	 * @return
	 */

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
		return TOTAL_ENTITY_COUNT++;
	}

	/**
	 * An interaction between a player and an item.
	 * This handles different types of items as well. 			<br>
	 * If pickup-able item - Item is added to player inventory	<br>
	 * If door item - Door is unlocked, if given certain parameters<br>
	 * @param playerID - ID of the player who is interacting
	 * @param itemID - ID of the item that the player has interacted with
	 * @return - True if the interaction was successful, false if not.
	 */
	public boolean interact(int playerID,int itemID){
		Player p = allPlayers.get(playerID);
		Entity i = floorList.get(p.getLocation().getFloor()).getEntity(itemID);
		System.out.println("Item i is " +(i==null));
		if(i == null){
			if(p.getInventory()[0]!=null||p.getInventory()[1]!=null){
				return dropItem(p, itemID);
			}else{
				return false;
			}
		}
		if(i instanceof Door){
			//If its a door, unlock it if possible
			Door door = (Door)i;
			return	unlockDoor(door,p);
		}else if(i instanceof Tool){
			//If its a tool, pick it up
			return pickUpItem(playerID,itemID);
		}else if(i instanceof Container){
			Container cont = (Container)i;
			Item randomI = cont.getRandomItem();
			if(randomI==null){
				return false;
			}
			if(randomI instanceof Tool){
				return p.pickUp((Tool)randomI);
			}
			return false;
		}else if(i instanceof NPC){
			if(i instanceof AttackNPC){
				return attackNPC(p, (AttackNPC)i);
			}
		}else if(i instanceof Portal){
			moveFloor(playerID, itemID);
			return true;
		}
		return false;
	}

	/**
	 * This methods is used when a player attacks an attackable NPC
	 * It damages the NPC's health by the value on the weapon and
	 * damages the player a little
	 *
	 * It then checks whether the NPC is alive and if not, removes it
	 * from the game
	 *
	 * If a player is not alive, it will respawn with full health
	 * on the bottom floor with full health
	 *
	 * Returns true if player had a weaon equipped to attack them
	 *
	 * @param p
	 * @param i
	 * @return
	 */
	private boolean attackNPC(Player p, AttackNPC i) {
		if(p.getEquippedTool() instanceof Weapon){
			Weapon w = (Weapon)p.getEquippedTool();
			w.interact(i); // weapon used on NPC
			p.attack(5); // does a little damage to NPC
			if(!i.isAlive()){
				Floor f = floorList.get(i.getLocation().getFloor());
				f.removeEntity(i);
			}
			if(!p.isAlive()){
				respawn(p);
			}
			return true;
		}
		return false;
	}

	/**
	 * Moves a dead player off their current floor
	 * to the base floor and regenerates their health
	 *
	 * @param p
	 */
	private void respawn(Player p) {
		Floor f = floorList.get(p.getLocation().getFloor());
		f.removePlayer(p);
		Floor newFloor = floorList.get(1);
		Location newLocation = new Location(3,-4,1);
		p.setLocation(newLocation);
		p.setHealth(100);
		newFloor.addPlayer(p);
	}

	/**
	 *This method checks if a player has a key equipped to unlock
	 *a door they click on. Returns true if their key can unlock the door.
	 *Unlocked doors are then deleted from the game so all players can pass through it
	 *
	 * @param door
	 * @param p
	 * @return
	 */
	private boolean unlockDoor(Door door, Player p) {
		door.interact(p);
		if(!door.isLocked()){
			System.out.println("Door is locked but shoould now be unlocked");
			Floor f = floorList.get(door.getLocation().getFloor());
			f.removeEntity(door);
			return true;
		}
		return false;
	}

	/**
	 * Allows a player to use an equipped item on themselves
	 * @param pID
	 * @param itemID
	 * @return
	 */
	public boolean useEquippedItem(int pID, int itemID){
		Player p = allPlayers.get(pID);
		p.useItem(itemID);
		return true;
	}

}

