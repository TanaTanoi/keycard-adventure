package gameObjects.player;

import java.util.List;

import gameObjects.world.Location;
/**
 * A riddleNPC has a question and stores an answer to this question.
 * Answering this question correctly may result either in information
 * or in a useful item
 *
 * @author craighhann
 *
 */

public class RiddleNPC extends NPC{

	private String question;
	private String answer;

	private List<Integer> playersTalkedTo;

	public RiddleNPC(String name, int health, Location loc, String question, String answer, String modelName, int ID) {
		super(name, loc, ID, modelName);
		this.question = question;
		this.answer = answer;
	}
}
