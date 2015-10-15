package gameObjects.player;

import gameObjects.world.Location;

/**
 * When clicked by a player, an InfoNPC should display a message stored in the location field.
 * Since we are using OPENGL, this message field stores a filepath to an image displaying the text
 * the NPC says
 *
 * @author craighhann
 *
 */

public class InfoNPC extends NPC {

	private String information;

	public InfoNPC(String name, Location loc, String information, String modelName, int ID) {
		super(name, loc, ID, modelName);
		this.information = information;
	}

	/**
	 * Returns the filepath to the image that the InfoNPC
	 * should display when clicked
	 * @return
	 */
	public String getInfo(){
		return information;
	}

}
