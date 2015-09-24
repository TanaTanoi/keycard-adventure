package gameObjects;

public abstract class Furniture implements Item {
	
	private String name;
	private String description;
	//private FurnitureStrategy strategy;
	
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public String getDescription(){
		return description;
	}

}
