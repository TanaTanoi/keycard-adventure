package gameObjects.world;

import gameObjects.objects.Furniture;
import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Potion;
import gameObjects.objects.Tool;
import gameObjects.objects.Weapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Parser {

	private static final int WORLD_SIZE = 100;
	GameWorld g;

	/**
	 * Open's the given file which contains a list
	 * a series of lines. Each line has a number indicating
	 * what floor level the file is to be for and a filepath/filename
	 * for the text file representing that floor
	 *
	 * @param filename is the name of the file to parse
	 */
	public static void parseWorld(String floorFiles, GameWorld g) {

		File config = new File(floorFiles); // opens config file
		Scanner outer;
		try {
			outer = new Scanner(config);

			while(outer.hasNextLine()){ // while there are still files to parse

				char[][] world = new char[WORLD_SIZE][WORLD_SIZE]; // creates new floorplan

				for(char[] ca:world){
					Arrays.fill(ca,'-'); // fills world plan with empty world character
				}

				int level = outer.nextInt(); // gets floor level
				String filename = outer.next(); // gets file path describing floor

				File f = new File(filename);

				try {
					Scanner s = new Scanner(f);
					List<Item> items = new ArrayList<Item>();
					while(s.hasNextLine()){
						switch(s.next()){
						case("WALL"): // line describes wall in world
							parseWall(s,world);
						break;
						case("PROP"): // describes furniture
							items.add(parseProp(s,level,g.setItemID()));
						break;
						case("TOOL"):
							items.add(parseTool(s,level,g.setItemID()));
						break;
						}
					}
					s.close();
					g.setFloor(world, level, items); // adds floor to game
					
				} catch (FileNotFoundException e) {e.printStackTrace(); }

			}
		} catch (FileNotFoundException e1) {e1.printStackTrace();}

	}

	private static Item parseTool(Scanner s, int level, int setItemID) {
		Tool t;

		String type = s.next();
		String name = s.nextLine();
		String description = s.nextLine();
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String modelName = s.next();

		switch(type){
		case("key"):
			t = new Key(name, description, l, modelName, setItemID);
			break;
		case("potion"):
			int effectP = s.nextInt();
			t = new Potion(name, description, l, effectP, modelName, level);
			break;
		case("weapon"):
			int effectW = s.nextInt();
			t = new Weapon(name, description, l, effectW, modelName, level);
			break;
		default:
			t = null;
		}

		return t;
	}

	private static Item parseProp(Scanner s, int level, int ID) {
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y, level);
		String modelName = s.next();

		Furniture f = new Furniture(l,modelName,ID);
		return f;
	}

	private static void parseWall(Scanner s, char[][]world){
		int startX = s.nextInt();
		int startY = s.nextInt();
		int endX = s.nextInt();
		int endY = s.nextInt();


		float changeX = (endX - startX)/100.0f;
		float changeY = (endY - startY)/100.0f;

		for(int i = 0; i < 100; i++){
			world[startY+(int)(changeY*i)][startX+(int)(changeX*i)] = 'X';
		}
	}


}
