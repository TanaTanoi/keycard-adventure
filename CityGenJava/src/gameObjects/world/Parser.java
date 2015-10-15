package gameObjects.world;

import gameObjects.objects.Container;
import gameObjects.objects.Door;
import gameObjects.objects.Entity;
import gameObjects.objects.Furniture;
import gameObjects.objects.FurnitureContainer;
import gameObjects.objects.Item;
import gameObjects.objects.Key;
import gameObjects.objects.Map;
import gameObjects.objects.Portal;
import gameObjects.objects.Potion;
import gameObjects.objects.Tool;
import gameObjects.objects.Weapon;
import gameObjects.player.InfoNPC;
import gameObjects.player.NPC;

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
					List<Entity> entities = new ArrayList<Entity>();
					while(s.hasNextLine()){
						switch(s.next()){
						case("WALL"): // line describes wall in world
							parseWall(s,world);
						break;
						case("PROP"): // describes furniture
							entities.add(parseProp(s,level,g.setItemID()));
						break;
						case("TOOL"):
							entities.add(parseTool(s,level,g.setItemID()));
						break;
						case("DOOR"):
							entities.add(parseDoor(s,level,g.setItemID()));
						break;
						case("PORTAL"):
							entities.add(parsePortal(s,level,g.setItemID()));
						break;
						case("CONTAINER"):
							entities.add(parseContainer(s,level,g.setItemID(),g));
						break;
						case("NPC"):
							entities.add(parseNPC(s,level,g.setItemID()));
						break;
						}
						if(!entities.isEmpty())
							System.out.println("Added a " + entities.get(entities.size()-1).getClass().getSimpleName());
					}

					s.close();
					g.setFloor(world, level, entities); // adds floor to game


				} catch (FileNotFoundException e) {e.printStackTrace(); }

			}
		} catch (FileNotFoundException e1) {e1.printStackTrace();}

	}

	/**
	 * Creates a NPC object from the following:<br>
	 * NPC [NAME] [TYPE] [X POS] [Y POS ] [Model Path] [*{Info} Dialog]
	 * @param s - Currently used scanner ("NPC" token has been consumed)
	 * @param level - Level to place NPC on
	 * @param setItemID - Global Entity ID for the NPC
	 * @return - And NPC to be placed in the floor
	 */
	private static NPC parseNPC(Scanner s, int level, int setItemID) {
		String name = s.next();
		String type = s.next();
		int xpos = s.nextInt();
		int ypos = s.nextInt();
		String modelpath = s.next();
		switch(type){
		case "INFO":
			String dialog = s.next();
			return new InfoNPC(name,new Location(xpos,ypos,level),dialog,modelpath,level);
			default:
				return null;
		}
	}
	/**
	 * Creates a portal object that pertains to the following structure:<br>
	 * PORTAL [Target floor] [X Pos] [Y Pos] [Model name]
	 * @param s - Currently in use scanner (Has consumed the "PORTAL" token)
	 * @param level - The floor level this portal will be a part of
	 * @param setItemID - The ID of the portal, set by the parser
	 * @return - The portal constructed by the parser.
	 */
	private static Portal parsePortal(Scanner s, int level, int setItemID) {
		int endFloor = s.nextInt();

		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y,level);
		String modelName = s.next();
		Portal p = new Portal(level,endFloor,l,modelName,setItemID);
		return p;
	}
	/**
	 * Parses a container object that follows the given format:<br>
	 * CONTAINER [NAME] 						<br>
	 * [DESC]									<br>
	 * [Item Limit] [X Pos] [Y Pos] [Model Path]<br>
	 * {										<br>
	 * [Entities to parse]						<br>
	 * }
	 * @param s - Currently in use scanner (Has consumed the "CONTAINER" token)
	 * @param level - The level this container will be assigned to
	 * @param setItemID - Global ID of the container
	 * @param g - Gameworld to be passed to tool parsers
	 * @return - A container to be added to the game world
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
	/**
	 * Parses a door and adds it to the floor:<br>
	 * DOOR [Name] 		<br>
	 * [Desription]		<br>
	 * [KeyName] 		<br>
	 * [xpos ] [ypos] [model path]
	 *
	 * @param s - current scanner ("DOOR" token has been consumed)
	 * @param level - Level this door will be assigned to
	 * @param setItemID- Globla ID of the door
	 * @return - Door to be added to the floor
	 */
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
	/**
	 * Parses a tool item in the following format:	<br>
	 * TOOL [Type] [Name]							<br>
	 * [Description]								<br>
	 * [x pos] [y pos] [model path] [image path] [*effect]
	 * @param s- Current scanner ("TOOL" has been consumed)
	 * @param level
	 * @param setItemID
	 * @return - Returns the tool to be added to the floor
	 */
	private static Item parseTool(Scanner s, int level, int setItemID) {
		Tool t;
		String type = s.next();
		String name = s.nextLine().trim();
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
		case("map"):
			t = new Map(name, description, l, modelName, setItemID, imageName);
		break;
		default:
			t = null;
		}
		return t;
	}

	/**
	 * Parses a prop item in the following format:
	 * PORTAL [Xpos] [Ypos] [Model path]
	 * @param s - Current scanner ("PORTAL" has been consumsed)
	 * @param level - Level to apply it to
	 * @param ID - Global ID of this prop
	 * @return - A prop to be added to the game world
	 */
	private static Item parseProp(Scanner s, int level, int ID) {
		int x = s.nextInt();
		int y = s.nextInt();
		Location l = new Location(x,y, level);
		String modelName = s.next();

		Furniture f = new Furniture(l,modelName,ID);
		return f;
	}

	/**
	 * Parses a wall and applies it to the supplied collision map
	 * Takes the following format:
	 * WALL [X1] [Y1] [X2] [Y2]
	 * @param s - Currently used scanner ("WALL" has been consumed)
	 * @param world - Collision map to apply the changes to
	 */
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
