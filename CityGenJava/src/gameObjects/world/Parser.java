package gameObjects.world;

import gameObjects.objects.Container;
import gameObjects.objects.Door;
import gameObjects.objects.Furniture;
import gameObjects.objects.FurnitureContainer;
import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Potion;
import gameObjects.objects.Tool;
import gameObjects.objects.Weapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
					List<Portal> portals = new ArrayList<Portal>();
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
						case("DOOR"):
							items.add(parseDoor(s,level,g.setItemID()));
						break;
						case("PORTAL"):
							portals.add(parsePortal(s,level));
							break;
						case("CONTAINER"):
							items.add(parseContainer(s,level,g.setItemID(),g));
						break;
						}
					}
					s.close();
					g.setFloor(world, level, items); // adds floor to game


				} catch (FileNotFoundException e) {e.printStackTrace(); }

			}
		} catch (FileNotFoundException e1) {e1.printStackTrace();}

	}

	private static Portal parsePortal(Scanner s, int level) {

		int startFloor = s.nextInt();
		int endFloor = s.nextInt();

		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String modelName = s.next();

		Portal p = new Portal(startFloor,endFloor,l,modelName);
		return p;
	}
	/**
	 * Parses a container object that follows the given format:
	 * CONTAINER [NAME]
	 * [DESC]
	 * [Item Limit] [X Pos] [Y Pos] [Model obj]
	 * {
	 * [Items to parse]
	 * }
	 * @param s
	 * @param level
	 * @param setItemID
	 * @param g
	 * @return
	 */
	private static Item parseContainer(Scanner s, int level, int setItemID, GameWorld g) {
		String name = s.nextLine();
		String description = s.nextLine();
		int limit = s.nextInt();
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String model = s.next();

		Container c = new FurnitureContainer(name,description,limit,l,model, setItemID);
		s.nextLine();
		s.next("\\{");
		s.nextLine();

		while(!s.hasNext("\\}")){
		Item i = null;

		switch(s.next()){
		case "TOOL":
			 i = parseTool(s,level,g.setItemID());
			break;
		case "CONTAINER":
			i =parseContainer(s,level,g.setItemID(),g);
		}
		c.addItem(i);
		}
		return c;

	}

	private static Item parseDoor(Scanner s, int level, int setItemID) {
		String name = s.nextLine();
		String description = s.nextLine();
		String keyName = s.nextLine();
		System.out.println(name);
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String modelName = s.next();

		// Note is assumed locked by default
		Door d = new Door(name,description,l,true,keyName,modelName,setItemID);
		return d;
	}

	private static Item parseTool(Scanner s, int level, int setItemID) {
		Tool t;

		String type = s.next();
		String name = s.nextLine();
		String description = s.nextLine();
		System.out.println(name);
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String modelName = s.next();
		String imageName= s.next();

		switch(type){
		case("key"):
			t = new Key(name, description, l, modelName, setItemID, imageName);
		break;
		case("potion"):
			int effectP = s.nextInt();
		t = new Potion(name, description, l, effectP, modelName, level, imageName);
		break;
		case("weapon"):
			int effectW = s.nextInt();
		t = new Weapon(name, description, l, effectW, modelName, level, imageName);
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
