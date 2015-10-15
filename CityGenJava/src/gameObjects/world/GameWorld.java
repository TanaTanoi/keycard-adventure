package gameObjects.world;

import gameObjects.objects.Container;
import gameObjects.objects.Door;
import gameObjects.objects.Entity;
import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Portal;
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

import org.omg.CORBA.Current;

import vec.Vector2;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

public class GameWorld {

	public static final int MAX_PLAYERS = 4;

	// For testing

	private Map<Integer,Player> allPlayers;
	private Map<Integer, Floor> floorList;
	private int TOTAL_PLAYER_COUNT = 0;
	private int TOTAL_ENTITY_COUNT = 0;

	public GameWorld(){
		init();
		//Parser is called on ClientController now, so we have more control over when parsing happens
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
		Player p = new Player(name, TOTAL_PLAYER_COUNT++);
		allPlayers.put(p.getID(),p); // adds player to game world
		// Now adds player to correct floor
		int floor = p.getLocation().getFloor();
		//floorList.get(floor).addPlayer(p);
		return TOTAL_PLAYER_COUNT-1;
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
	public void setFloor(char[][] floor, int level, List<Entity> items){
		Floor f = new Floor(level, floor, items);
		System.out.println("Adding floor at " + level + " with " + items.size() + " items");
		floorList.put(level, f);
	}
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
		System.out.println(l.getX() + " " + l.getY());
		for(Entity i: floorList.get(l.getFloor()).getEntities()){
			Location iloc = i.getLocation();
			//vector
			Vector2 ent_to_pl = new Vector2(iloc.getX()-l.getX(),iloc.getY()-l.getY());
			ent_to_pl = ent_to_pl.unitVector();
			Vector2 p_dir = new Vector2(
					(float)-Math.sin(Math.toRadians(rot)),
					(float)Math.cos(Math.toRadians(rot)));
			p_dir = p_dir.unitVector();
			System.out.println("P_DIR : " +p_dir.x + " " + p_dir.y);
			System.out.println("ENT_T : " +ent_to_pl.x + " " + ent_to_pl.y);
			System.out.println("ENT_L : " +l.getX() + " " + l.getY());

			float anglediff = p_dir.dot(ent_to_pl.mul(1.0f));
			System.out.println("Angle diff  " + (Math.round(anglediff*10)/10.0f));
			if(iloc.distance(l)<radius&&Math.round(anglediff*10.0f)/10.0f==1){
				System.out.println("We have selected " + i.getName());
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
	public boolean dropItem(Player p){
		Item i = p.drop();
		if(i == null){
			return false;
		}
		Location dropLoc = 	new Location(p.getLocation().getX() + 1, p.getLocation().getY(), p.getLocation().getFloor());
		i.setLocation(dropLoc);
		Floor f = floorList.get(i.getLocation().getFloor());
		f.addEntity(i);
		return true;
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
		System.out.println("Player ID " + playerID + "  Item ID " + itemID);
		Player p = allPlayers.get(playerID);
		//		Item i = floorList.get(p.getLocation().getFloor()).getItem(itemID);
		Entity i = floorList.get(p.getLocation().getFloor()).getEntity(itemID);
		if(i == null){System.out.println("Null caught");return false;}
		System.out.println(i.toString());
		if(i instanceof Door){
			//If its a door, unlock it if possible
			Door door = (Door)i;
			//door.interact();//TODO get player's equipt item and make it interact with door. If door unlocks, return true
			return false;//FIXME change when implemented (to true)
		}else if(i instanceof Tool){
			//If its a tool, pick it up
			return pickUpItem(playerID,itemID);
		}else if(i instanceof Container){
			Container cont = (Container)i;
			//pickUpItem(playerID,cont.getRandomItem().getID());
			Item randomI = cont.getRandomItem();
			if(randomI==null){
				return false;
			}

			if(randomI instanceof Tool){
				return p.pickUp((Tool)randomI);
			}
			else{
				// Is a container OMG help
			}
			return true;
		}else if(i instanceof NPC){

		}else if(i instanceof Portal){
			moveFloor(playerID, itemID);
			return true;
		}

		return false;
	}


}

