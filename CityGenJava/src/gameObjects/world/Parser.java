package gameObjects.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

	private static final int WORLD_SIZE = 100;

	/**
	 * Open's the given file and parses
	 * all the information to create the gameworld.
	 *
	 * @param filename is the name of the file to parse
	 */
	public static char[][] parseWorld(String filename, GameWorld g) {
		char[][] world = new char[WORLD_SIZE][WORLD_SIZE];
		for(char[] ca:world){
			Arrays.fill(ca,'-');
		}
		File f = new File(filename);

		try {
			Scanner s = new Scanner(f);
			while(s.hasNextLine()){
				switch(s.next()){
				case("WALL"):
					parseWall(s,world);
				break;
				case("PROP"):

					break;

				}
			}
		} catch (FileNotFoundException e) {e.printStackTrace(); }

		return world;
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

	public static void main(String args[]){
		char[][] test = parseWorld("Basefloor.txt",null);
		for(char[] ca: test){
			for(char c:ca){
				System.out.print(c);
			}
			System.out.println();
		}
	}

}
