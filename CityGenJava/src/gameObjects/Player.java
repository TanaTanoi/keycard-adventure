package gameObjects;

/**
 * The player class describes a character in the game
 * that is controlled by a user 
 * 
 * @author Hannah
 *
 */

public class Player implements Character{
	
	String name; // name selected by user
	Item [] inventory; // items  that the player carries 
	//Location l; // describes player's whereabouts in the game world
	
	public Player(String name){
		this.name = name;
	}
	
	/**
	 * Moves the player's position in the game world
	 * @param x is the x direction to move the player
	 * @param z is the z direction to move the player
	 */
	public void move(int x, int z){
		// Need to do something here
	}
	
	public Item talk(NPC npc){
		
	}
	
}
