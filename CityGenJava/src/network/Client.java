package network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ClientController;
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
			System.out.println("Encounted network problem");
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
	 * Disconnects this client from the server by stopping the client loop and sending a disconnect message.
	 * Currently no way to reconnect afterwards.
	 */
	public void disconenct(){
		connected = false;
	}

	public boolean attemptConnect() throws IOException{
		String userIn;
		String serverInput;
		System.out.println("Connecting to " + gameHost);
		clientSocket = new Socket(gameHost,port);
		serverOut = new DataOutputStream(clientSocket.getOutputStream());
		serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		//send initial login packet containing name
		String name  = "George";
		serverOut.writeBytes(name+"\n");
		serverOut.flush();

		//receive ID number
		serverInput = serverIn.readLine();

		//set game's current player to a new player. Appends the name to the serverInput
		//To allow for simple parsing
		NetworkDecoder.decode(game,serverInput + " " + name);
		return true;
	}


	/**
	 * This is the thread's main loop. It will set up the connection to the server,
	 * send an intro package, then continuously send player-position related packages.
	 *
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

		}catch(Exception e){
			System.out.println("PROBLEM");
			e.printStackTrace();
		}
	}
}


