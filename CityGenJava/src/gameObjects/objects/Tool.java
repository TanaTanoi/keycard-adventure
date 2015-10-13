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

	private int ID;

	private String name;
	private String description;
	private String model;
	private Location loc;
	private InteractStrategy interactStrategy;
	private String image;

	public Tool(String name, String desc, Location loc, String model, int ID, String image){
		this.name = name;
		description = desc;
		this.loc = loc;
		this.model = model;
		this.ID = ID;
		this.image = image;
	}

	/**
	 * SetStrategy sets the interact strategy for this tool
	 * @param strat is the interact strategy
	 */
	public void setStrategy(InteractStrategy strat){
		interactStrategy = strat;
	}

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

	@Override
	public void setLocation(Location l){
		loc = l;
	}

	@Override
	public String getModelName(){
		return model;
	}

	@Override
	public int getID(){
		return ID;
	}

	public String getImagePath(){
		return image;
	}

}
