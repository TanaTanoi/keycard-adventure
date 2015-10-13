package gameObjects.player;

import gameObjects.world.Location;

public class InfoNPC extends NPC {

	private String information;

	public InfoNPC(String name, int health, Location loc, String information) {
		super(name, health, loc);
		this.information = information;
	}

	public String getInfo(){
		return information;
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
