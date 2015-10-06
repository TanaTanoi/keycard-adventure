package network;

import gameObjects.world.GameWorld;
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
	 * The server is expected to divide it by 100 when received.
	 *
	 * @param game_client - The game client in which to get an output from.
	 * @return - String containing the current player's x,y and their ID
	 */
	public static String getPlayerOutput(ClientController game_client){
		float[] info = game_client.getPlayerInfo();
		return getPlayerString(info);
	}


	public static String getPlayerDisconnect(ClientController game_client){

		return "DISC " + (int)game_client.getPlayerInfo()[0];
	}
	/**
	 * Decodes an input from the server and applies the changes to the supplied game client.
	 *
	 *@param game_client - The game client that the changes will be appleid to.
	 * @param input - String directly from the server.
	 */
	public static void decode(ClientController game_client, String input){
//		System.out.println("Decoding " + input);
		Scanner sc = new Scanner(input);
		while(sc.hasNext()){
			String next = sc.next();
			//if input is player
			if(next.equals("P")){
				parsePlayer(sc,game_client);
			}
		}
		sc.close();
	}
	/**
	 * This method parses a player string which is taken from the format:
	 * 			P [Player ID] [Player X *100] [Player Y * 100] [Player Rotation]
	 * Scanner assumes the P has already been consumed, i.e. the next token should be the ID
	 * The *100 is to convert from float to int with an accuracy of 0.01, then back to floats
	 * @param sc - Scanner of the input from the client
	 * @param game_client - Game client to modify to update the players
	 */
	public static void parsePlayer(Scanner sc,ClientController game_client){
		//if(sc.next()!="P"){throw new IllegalArgumentException("Expected P as first input! -->" + sc.next() + " " + sc.next() );}
		try{
			game_client.updatePlayer(
					Integer.parseInt(sc.next()),
					Integer.parseInt(sc.next())/100.0f,
					Integer.parseInt(sc.next())/100.0f,
					Integer.parseInt(sc.next()));
		}catch(NumberFormatException e){
			//if one of the things fail, print as problem and skip it
			System.out.println("Error! Package was not correct. Expected int,int,int,int");
			e.printStackTrace();
		}
	}


	/*----------------------------------*\
	 * 		SERVER'S DECODE METHODS 	*
	\*----------------------------------*/

	/**
	 * Decode the input from a certain player.
	 * If the input is invalid, can throw exceptions.
	 * @param game - The Game World that will be updated by this method call.
	 * @param input - The direct input from the client
	 * @param player - The player who sent the input
	 */
	public static void decodeClientInput(GameWorld game,String input, int player){
		System.out.println("Received "+ input + " from player " + player);
		Scanner sc = new Scanner(input);
		while(sc.hasNext()){
			String next = sc.next();
			//Decode the player's Player update input
			if(next.equals("P")){
				try{
					game.updatePlayerInfo(Integer.parseInt(sc.next()),	//ID
							Integer.parseInt(sc.next())/100.0f,			//X*100
							Integer.parseInt(sc.next())/100.0f,			//y*100
							Integer.parseInt(sc.next()));				//Rotation
				}catch(NumberFormatException e){
					System.out.println("Error! Received bad input |" + input + "| couldn't parse into (int,int,int,int) ");
				}
			}else if(next.equals("DISC")){
				//TODO handle disconnect
			}
		}
		sc.close();
	}

	/**
	 * Prepares the output to be sent to all clients, based on the input GameWorld
	 *
	 * @param world - The world the package will be based on.
	 * @return - A string that can be sent to all clients connected to the server.
	 */
	public static String prepPackage(GameWorld world){
		//Firstly, add all player positions to the string
		List<float[]> players = world.getPlayerInfos();
		StringBuilder sb = new StringBuilder();
		for(float[] p: players){
			sb.append(getPlayerString(p));
			sb.append(" ");
		}
		/* Room for extra sections of the string can go here*/
		return sb.toString();
	}


	/*----------------------------------*\
	 * 		GENERAL HELPER METHODS 		*
	\*----------------------------------*/

	/**
	 * Prepares a string that can be sent to a client or server based on the provided information
	 * @param playerInfo
	 * @return
	 */
	public static String getPlayerString(float[] playerInfo){
		if(playerInfo.length!=4)throw new IllegalArgumentException("Method only accepts playerinfo of length 4: ID,X,Y,ROT");

		playerInfo[1]*=100.0f;
		playerInfo[2]*=100.0f;
		//P [ID] [X*100] [Y*100] [ROT]
		StringBuilder sb = new StringBuilder();
		sb.append("P ");
		sb.append((int)playerInfo[0]);
		sb.append(" ");
		sb.append((int)playerInfo[1]);
		sb.append(" ");
		sb.append((int)playerInfo[2]);
		sb.append(" ");
		sb.append((int)playerInfo[3]);
		return sb.toString();
	}

}
