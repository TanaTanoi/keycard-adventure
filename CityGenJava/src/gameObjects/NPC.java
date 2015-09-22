package gameObjects;

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
public class NPC implements Character {
	
	private String name;
	
	public NPC(String name){
		
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
	public void move(int x, int z) {
		// TODO Auto-generated method stub
		
	}

}
