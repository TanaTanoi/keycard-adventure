package network;

import gameObjects.objects.Entity;
import gameObjects.objects.Item;
import gameObjects.world.GameWorld;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
	 * Get the string to send to the server that represents the player's coordinates and ID and rotation
	 * The coordanites are multiplied by 100 and casted to ints for simple broadcasting.
	 * The server is expected to divide it by 100 when received.
	 *
	 * @param game_client - The game client in which to get an output from.
	 * @return - String containing the current player's x,y and their ID. Also adds any interaction calls
	 */
	public static String getPlayerOutput(ClientController game_client){
		float[] info = game_client.getPlayerInfo();
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(getPlayerString(info));

		Entity pickedUp = game_client.getToInteract();
		if(pickedUp !=null){
			//if we want to pick up an item, send a pick up request in the form INTERACT [Item ID] [Player ID]
			toReturn.append(" ");
			if(game_client.useOnSelf){
				toReturn.append("USE ");
			}else{
				toReturn.append("INTERACT ");
			}
			toReturn.append(pickedUp.getID());
			toReturn.append(" ");
			toReturn.append(game_client.getCurrentPlayer().getID());
		}
		return toReturn.toString();
	}

	/**
	 * Gets a disconnection string that is sent when a player disconnects,
	 * formally, from the server.
	 * The message is as follows
	 * 			DISC [Player ID]
	 * @param game_client - Current player to get the ID from
	 * @return - String that can be sent to the server
	 */
	public static String getPlayerDisconnect(ClientController game_client){
		return "DISC " + (int)game_client.getPlayerInfo()[0];
	}
	/**
	 * Decodes an input from the server and applies the changes to the supplied game client.
	 *
	 * @param game_client - The game client that the changes will be applied to.
	 * @param input - String directly taken from the server.
	 */
	public static void decode(ClientController game_client, String input){
		Scanner sc = new Scanner(input);
		while(sc.hasNext()){
			String next = sc.next();
			//if input is player
			if(next.equals("P")){
				parsePlayer(sc,game_client);

			}else if(next.matches("-*\\d")){
				//if the returned value is JUST a number, then its a reply from the server for connection
				int playerID = Integer.parseInt(next);
				if(playerID >=0){
					//if acceptable ID number
					game_client.setCurrentPlayer(sc.next(), playerID);
				}else if(playerID == -1){//-1 means max players has been reached
					throw new IllegalArgumentException("Server has max amount of players already!");
				}else if(playerID == -2){//-2 means game has already started. TODO actually implement this
					throw new IllegalArgumentException("Game has already started. Connection refused.");
				}
			}else if(next.equals("INTERACT")){
				int itemID = sc.nextInt();
				int playerID = sc.nextInt();
				System.out.println("INTERACT " +itemID + " " +playerID);
				game_client.interact(playerID, itemID);
			}else if(next.equals("USE")){
				int itemID = sc.nextInt();
				int playerID = sc.nextInt();
				System.out.println("USE " +itemID + " " +playerID);
				game_client.use(playerID, itemID);
			}else if(next.equals("DISC")){
				int playerID = sc.nextInt();
				game_client.removePlayer(playerID);
				//TODO handle removing a player from the world

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
	 * Decode the input from a certain player. Returns false if the player
	 * has made a formal disconnect
	 * If the input is invalid, can throw exceptions.
	 * If the input is null (i.e. hard disconnect from player) returns false
	 * @param game - The Game World that will be updated by this method call.
	 * @param input - The direct input from the client
	 * @param player - The player who sent the input
	 * @param approvedCommands - A set of extra commands to be added to, if needed
	 * @return - True if the player is still connected.
	 */
	public static boolean decodeClientInput(GameWorld game,String input, int player,Set<String> approvedCommands){
		if(input==null){
			System.out.println("INPUT IS NULL, REMOVING PLAYER");
			game.removePlayer(player);
			approvedCommands.add("DISC "+player);
			return false;
			}
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
				try{
					int playerID = Integer.parseInt(sc.next());
					//TODO handle disconnect of this player on the game world.
					game.removePlayer(playerID);
					approvedCommands.add(input);
					return false;
				}catch(NumberFormatException e){
					System.out.println("Error! Received bad input |" + input + "| couldn't parse into (int) ");
				}
			}else if(next.equals("INTERACT")){
				//INTERACT [ITEM ID] [PLAYER ID of play who will receive item]
				int itemID = sc.nextInt();
				int playerID = sc.nextInt();
				System.out.println("Interact call " + itemID + " " + playerID);
				if(game.interact(playerID, itemID)){
					approvedCommands.add(next +" "+ itemID + " " + playerID);
				}
			}else if(next.equals("USE")){
				//USE [ITEM ID] [PLAYER ID of play who will receive item]
				int itemID = sc.nextInt();
				int playerID = sc.nextInt();
				if(game.useEquippedItem(playerID, itemID)){
					approvedCommands.add(next +" "+ itemID + " " + playerID);
				}

			}
		}
		sc.close();
		return true;
	}

	/**
	 * Prepares the output to be sent to all clients, based on the input GameWorld
	 * as well as commands prepared earlier (via approvedCommands).
	 *
	 * @param world - The world the package will be based on.
	 * @param approvedCommands -The additional commands to add to the package. THIS EMPTIES THE LIST
	 * @return - A string that can be sent to all clients connected to the server.
	 */
	public static String prepPackage(GameWorld world,Set<String> approvedCommands){
		//Firstly, add all player positions to the string
		List<float[]> players = world.getPlayerInfos();
		StringBuilder sb = new StringBuilder();
		for(float[] p: players){
			sb.append(getPlayerString(p));
		}

		//Add each approved command to the string to dispatch to all players, then remove them
		if(!approvedCommands.isEmpty()){
			Iterator<String> itr = approvedCommands.iterator();
			while(itr.hasNext()){
				System.out.println("Appending extra to command");
				sb.append(itr.next());
				sb.append(" " );
				itr.remove();
			}
		}
		assert approvedCommands.isEmpty();
		/* Room for extra sections of the string can go here*/
		return sb.toString();
	}

	/*----------------------------------*\
	 * 		GENERAL HELPER METHODS 		*
	\*----------------------------------*/

	/**
	 * Prepares a string that can be sent to a client or server based on the provided information
	 * @param playerInfo - Player info to be converted
	 * @return - String in decoder format that represents a player
	 */
	public static String getPlayerString(float[] playerInfo){
		if(playerInfo.length!=4)throw new IllegalArgumentException("Method only accepts playerinfo of length 4: ID,X,Y,ROT");
		playerInfo[1]*=100.0f;
		playerInfo[2]*=100.0f;
		//P [ID] [X*100] [Y*100] [ROT]
		StringBuilder sb = new StringBuilder();
		sb.append("P ");
		for(int i =0;i<4;i++){
			sb.append((int)playerInfo[i]);
			sb.append(" ");
		}
		return sb.toString();
	}

}
