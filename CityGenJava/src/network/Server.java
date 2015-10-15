package network;
import gameObjects.world.GameWorld;
import gameObjects.world.Parser;
import graphics.applicationWindow.Window;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL;

/**
 * This controls the server's version of the game world as well as the networking. <br>
 * It works by waiting until we get the maximum amount of players in the server, then attaches
 * all of the clients to a separate thread that requests information from all clients
 * then distributes a package based on the information it received to all players.
 * All decoding is delegated to the NetworkDecoder static class.
 * @author Tana
 */
public class Server {

	public static final int port = 32768;
	static GameWorld world;//FIXME Find a way to have this private. Its not great having it package level




	/**
	 * The main deals with the connection of GameWorld.MAX_PLAYERS amount of players, then sending
	 * the information to the thread. It will not receive or distribute any information until the max amount of players
	 * has been connected. This prevents many game logic related errors.
	 * @param argv - Expects no arguments
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception{
		Window w = new Window();
		GL.createCapabilities(true); // valid for latest build
		//initialise client on this port
		ServerSocket clientInteractSocket = new ServerSocket(port);
		String IPInfo = clientInteractSocket.getInetAddress().getHostAddress();
		//Set up thread that controls client-server relations
		ClientThread ct = new ClientThread();

		world = new GameWorld();
		Parser.parseWorld("realfloorconfig.txt", world);
		//Constantly accept clients
		w.destory();
		int total_connections = 0;
		while(total_connections < GameWorld.MAX_PLAYERS){
			//LOGIN PROTOCOL -> Accept user -> take name from user -> send new ID to user -> add user to list

			//Accept a client
			Socket cl = clientInteractSocket.accept();
			System.out.println("Accepted client :" +ct.getName());

			//Accept the name and make a new player for it
			BufferedReader clientIn =new BufferedReader(
					new InputStreamReader(cl.getInputStream()));
			String clInput = clientIn.readLine();

			//Add player to the game world. If the server has the max amount of players, it returns -1
			int playerID = world.addNewPlayer(clInput);

			//Return an ID associated with this player.
			DataOutputStream clientOut = new DataOutputStream(cl.getOutputStream());
			clientOut.writeBytes(playerID+"\n");

			//Accept a client (if applicable) and add it to the client thread
			if(playerID>=0){		//won't accept the client if the gameworld said no
				ct.add(cl);
			}
			total_connections++;
		}
		ct.start();

	}

}

/**
 * This class controls the main server tick. It continuously accepts inputs from all clients
 * then confirms this information is valid, then distributes those messages to all other clients.<br>
 * When a user makes an action, it doesn't happen on the local client until it has passed through this message
 * (With the exception of NPCs talking).
 * @author Tana
 *
 */
class ClientThread extends Thread{
	List<Socket> connections;
	String clInput;
	ClientThread(){
		super();
		this.connections = new ArrayList<Socket>();
	}

	/**
	 * This method runs the main server loop. <br>
	 * Get information from clients -> Process information -> Send package to all information
	 *
	 *
	 */
	public void run(){
		Set<String> approvedCommands = new HashSet<String>();//A list of commands that have been approved to be sent
		try{
			while(true){
				Thread.sleep(100);
				while(!connections.isEmpty()){
//					System.out.println("Reading inputs");

					//get the input from each client
					for(int i =0;i<connections.size();i++){
						BufferedReader clientIn =new BufferedReader(
								new InputStreamReader(connections.get(i).getInputStream()));
						clInput = clientIn.readLine();
						if(!NetworkDecoder.decodeClientInput(Server.world,clInput,i,approvedCommands)){
							connections.get(i).close();
							connections.remove(i);
							i--;
						}
					}
					//Generate output to send to all clients
					String output = NetworkDecoder.prepPackage(Server.world,approvedCommands);
					//Send output to all clients
					for(int i = 0; i < connections.size();i++){
						DataOutputStream clientOut = new DataOutputStream(connections.get(i).getOutputStream());
						clientOut.writeBytes(output+"\n");

					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{

		}
	}

	/**
	 * Adds a client to the game loop. This shouldn't be called before run has been called.
	 * @param newClient - Client socket to add.
	 */
	public void add(Socket newClient){
		connections.add(newClient);
	}

}