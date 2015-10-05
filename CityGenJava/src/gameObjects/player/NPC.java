package gameObjects.player;

import gameObjects.objects.Item;
import gameObjects.world.Location;

/**
 * The npc class describes a character that is not playable
 * These characters can be talked to by players in the game world.
 *
 * Talking may result in a puzzle or riddle being asked which
 * leads to either information or a useful item
 *
 * NPC's may either be stationary or walk a circuit around the game world
 *
 * @author Hannah
 *
 */
public abstract class NPC implements Character {

	private String name;
	private int health;
	private Location loc;
	private int orientation;

	public NPC(String name, int health, Location loc){
		this.name = name;
		this.health = health;
		this.loc = loc;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Item talk(Character c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(float x, float z) {
		loc.setX(x);
		loc.setY(z);
	}

	public void attack(int damage){
		health -= damage;
	}

	@Override
	public boolean isAlive(){
		return health > 0;
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public int getOrientation(){
		return orientation;
	}

	@Override
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	@Override
	public void changeOrientation(int change) {
		orientation += change;

		if(orientation < 0){
			orientation += 360;
		}
		else if(orientation > 359){
			orientation -= 360;
		}
	}

}
