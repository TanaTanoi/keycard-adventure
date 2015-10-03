package network;

import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ClientController;

/**
 * This class handles the decoding of information from the server.
 * The methods are static to prevent the need for storing variables
 * in this class.
 * @author Tana
 *
 */
public class NetworkDecoder {

	/**
	 * Get the string to send to the server that represents the player's coordinates and ID
	 * The coordanites are multiplied by 100 and casted to ints for simple broadcasting.
	 * The server is expected to dividie it by 100 when received.
	 *
	 * @param game_client - The game client in which to get an output from.
	 * @return - String containing the current player's x,y and their ID
	 */
	public static String getPlayerOutput(ClientController game_client){
		//0:ID 1:X 2: y
		float[] info = game_client.getPlayerInfo();
		assert info.length == 3;
		info[1]*=100.0f;
		info[2]*=100.0f;
		String output = ((int)info[0]) + " " + ((int)info[1]) +  " " + ((int)info[2]);
		return output;
	}

	/**
	 * Decodes an input from the server and applies the changes to the supplied game client.
	 *
	 *@param game_client - The game client that the changes will be appleid to.
	 * @param input - String directly from the server.
	 */
	public static void decode(ClientController game_client, String input){
		System.out.println("Decoding " + input);
		List<float[]> players = new ArrayList<float[]>();
		//int[][] players = new int[][3];
		//get the players information and place into array
		Scanner sc = new Scanner(input);
		while(sc.hasNext()){
			//if player ID
			if(sc.hasNextInt()){
				float[] p = new float[3];
				p[0] = Integer.parseInt(sc.next());
				p[1] = (float)(Integer.parseInt(sc.next())/100.0f);
				p[2] = (float)(Integer.parseInt(sc.next())/100.0f);
				if(p[0]!=game_client.getPlayerInfo()[0]){
					players.add(p);
				}
			}
		}
		sc.close();
		//copy all players information apart from self, into array
		float[][] p = new float[players.size()][3];
		for(int i =0; i< players.size();i++){
			p[i] = players.get(i);
		}
		//Send updated player pos' to game
		game_client.updatePlayers(p);
	}


	/*----------------------------------*\
	 * 		SERVER'S DECODE METHODS 	*
	\*----------------------------------*/

	/**
	 * Decode the input from player provided.
	 * If the input is invalid, can throw exceptions. FIXME
	 * @param game - The Game World that will be updated by this method call.
	 * @param input - The direct input from the client
	 * @param player - The player who sent the input
	 */
	public static void decode(GameWorld game,String input, int player){
		System.out.println("Received "+ input + " from player " + player);
		Scanner sc = new Scanner(input);
		int[] p = new int[3];
		while(sc.hasNext()){
			//if player ID
			if(sc.hasNextInt()){

				p[0] = Integer.parseInt(sc.next());
				p[1] = Integer.parseInt(sc.next());
				p[2] = Integer.parseInt(sc.next());
			}
		}
		sc.close();
		game.updatePlayerInfo(p[0], (float)(p[1]/100.0f), (float)(p[2]/100.0f));
	}


	/**
	 * Prepares the output to be sent to all clients, based on the input GameWorld
	 *
	 * @param world - The world the package will be based on.
	 * @return - A string that can be sent to all clients connected to the server.
	 */
	public static String prepPackage(GameWorld world){
		List<Player> players = world.getPlayers();
		StringBuilder sb = new StringBuilder();
		for(Player p: players){
			sb.append((int)p.getID());
			sb.append(" ");
			Location loc = p.getLocation();
			sb.append((int)(loc.getX()*100.0f) + " ");
			sb.append((int)(loc.getY()*100.0f) + " ");
		}
		return sb.toString();
	}


}
