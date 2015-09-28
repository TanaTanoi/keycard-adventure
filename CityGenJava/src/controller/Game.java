package controller;

import gameObjects.world.GameWorld;

public class Game {
	
	GameWorld world;
	
	public Game(String filename){
		world = new GameWorld(filename);
	}

	public static void main(String[] args) {
		new Game("gameWorld.txt");
	}

}
