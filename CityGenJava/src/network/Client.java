package network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ClientController;
/**
 * This class controls sending information to the server over the network <br>.
 * This works by constantly polling the ClientController for information to send to the server.
 * It will always send player movement, with an optional package that tells the server of an action.
 * @author Tana
 *
 */
public class Client extends Thread{

	private  int port = 32768;
	Socket clientSocket;
	ClientController game;

	private DataOutputStream serverOut;
	private BufferedReader serverIn;

	String  gameHost = InetAddress.getLocalHost().getHostAddress();
	private boolean connected = true;
	/**
	 * Standard constructor that uses default port (32768) and local host as IP
	 * @throws Exception - If unable to connect to the server via standard port (32768)
	 */
	public Client(ClientController game) throws Exception {
		this.game = game;
		try{
			attemptConnect();
		}catch(IOException e){
			System.out.println("Encounted network problem \n Could not connect.");
			e.printStackTrace();

		}
		start();
	}

	/**
	 * Constructor that takes a different IP to standard
	 * @throws Exception - If unable to connect to the server via standard port (32768)
	 */
	public Client(ClientController game, String IP) throws Exception {
		System.out.println("Conneting to " +IP);
		gameHost = IP;
		this.game = game;

		try{
			attemptConnect();
		}catch(IOException e){
			System.out.println("Encounted network problem");
			e.printStackTrace();

		}
		start();
	}

	/**
	 * Disconnects this client from the server by stopping the client loop and sending a disconnect message. <br>
	 * <b>There is no way to reconnect afterwards.</b>
	 */
	public void disconenct(){
		connected = false;
	}

	/**
	 * This method attempts to create a connection to the server.
	 * @return - True if the connection is successful.
	 * @throws IOException - If the connection failed, throws IOException
	 */
	public void attemptConnect() throws IOException{
		String serverInput;
		System.out.println("Connecting to " + gameHost);
		clientSocket = new Socket(gameHost,port);
		serverOut = new DataOutputStream(clientSocket.getOutputStream());
		serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		//send initial login packet containing name
		String name  = "George";
		serverOut.writeBytes(name+"\n");
		serverOut.flush();

		//receive ID number for the player
		serverInput = serverIn.readLine();

		//set game's current player to a new player. Appends the name to the serverInput
		//To allow for simple parsing
		NetworkDecoder.decode(game,serverInput + " " + name);
	}


	/**
	 * This is the thread's main loop. This assumes a connection to the server has been established, then
	 * continuously streams information from the ClientController and then receives information back from the server.
	 */
	public void run() {
		try{
			String userIn;
			String serverInput;
			//assume regular packet loop
			while(connected){
				//send input containing player's information
				userIn = NetworkDecoder.getPlayerOutput(game);
				serverOut.writeBytes(userIn+"\n");
				serverOut.flush();

				//receive input pertaining to the other players in the system
				serverInput = serverIn.readLine();
				NetworkDecoder.decode(game,serverInput);
			}
			//Player has disconnected
			serverOut.writeBytes(NetworkDecoder.getPlayerDisconnect(game));
			serverOut.flush();

		}catch(IOException e){
			System.out.println("Problem communicating with server");
		}
	}
}


