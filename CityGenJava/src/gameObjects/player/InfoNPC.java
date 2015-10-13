package gameObjects.player;

import gameObjects.world.Location;

public class InfoNPC extends NPC {

	private String information;

	public InfoNPC(String name, int health, Location loc, String information, String modelName, int ID) {
		super(name, loc, ID, modelName);
		this.information = information;
	}

	public String getInfo(){
		return information;
	}

}
