package controller;

import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import graphics.View;

public class Game {
	
	GameWorld world; // Model
	Player current; // Player giving controls
	View view; // view
	
	public Game(String filename){
		world = new GameWorld(filename);
		view = new View(this);
		// need to make a spawn method for new player
	}	

	public static void main(String[] args) {
		new Game("gameWorld.txt");
	}

}
