package gameObjects.player;

import java.util.List;

import gameObjects.world.Location;

public class RiddleNPC extends NPC{

	private String question;
	private String answer;

	private List<Integer> playersTalkedTo;

	public RiddleNPC(String name, int health, Location loc, String question, String answer) {
		super(name, health, loc);
		this.question = question;
		this.answer = answer;
	}


}
