package gameObjects.objects;

import gameObjects.player.Character;
import gameObjects.strategies.InteractStrategy;
import gameObjects.world.Location;

/**
 * The tool class describes a type of item that can be "equipped".
 * When an item is equipped, it can be used to interact with the game  world
 * @author Hannah
 *
 */

public abstract class Tool implements Item {

	private String name;
	private String description;
	private Location loc;
	private InteractStrategy interactStrategy;

	public Tool(String name, String desc, Location loc){
		this.name = name;
		description = desc;		
		this.loc = loc;
	}

	/**
	 * SetStrategy sets the interact strategy for this tool
	 * @param strat is the interact strategy
	 */
	public void setStrategy(InteractStrategy strat){
		interactStrategy = strat;
	}

	/**
	 * Equip makes the tool the current selected tool
	 */
	public abstract void equip();

	@Override
	public void interact(Character c){
		if(interactStrategy != null){
			interactStrategy.useOnPlayer(c);
		}
	}
	
	
	@Override
	public void interact(Item i){
		if(interactStrategy != null){
			interactStrategy.interact(i);
		}
	}
	
	/**
	 * Drop releases the object from a player
	 * and places it in the game world
	 */
	public void drop(){
		
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getDescription(){
		return description;
	}
	
	@Override 
	public Location getLocation(){
		return loc;
	}

}
