package gameObjects;

/**
 * The tool class describes a type of item that can be "equipped".
 * When an item is equipped, it can be used to interact with the game  world
 * @author Hannah
 *
 */

public abstract class Tool implements Item {

	private String name;
	private String description;
	private InteractStrategy interactStrategy;

	public Tool(String name, String desc){
		this.name = name;
		description = desc;		
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

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getDescription(){
		return description;
	}

}
