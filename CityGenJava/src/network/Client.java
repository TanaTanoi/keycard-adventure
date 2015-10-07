package network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ClientController;
public class Client extends Thread{

	private  int port = 4444;
	Socket clientSocket;
	ClientController game;
	InetAddress gameHost = InetAddress.getLocalHost();
	private boolean connected = true;
	/**
	 * Standard constructor that uses default port (4444) and local host as IP
	 * @throws Exception - If unable to connect to the server via standard port (4444)
	 */
	public Client(ClientController game) throws Exception {
		this.game = game;
		start();
	}

	/**
	 * Constructor that takes a different IP to standard
	 * @throws Exception - If unable to connect to the server via standard port (4444)
	 */
	public Client(ClientController game, String IP) throws Exception {
		this.game = game;
		gameHost = InetAddress.getByName(IP);
		new Client(game);
	}

	/**
	 * Constructor that takes a different port to standard.
	 * @param port - The port to connect through
	 * @throws Exception - If unable to connect to the server via given port
	 */
	public Client(ClientController game, String IP,int port) throws Exception{
		this.port = port;
		new Client(game, IP);
	}

	/**
	 * Disconnects this client from the server by stopping the client loop and sending a disconnect message.
	 * Currently no way to reconnect afterwards.
	 */
	public void disconenct(){
		connected = false;
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
			clientSocket = new Socket(gameHost,port);
			DataOutputStream serverOut = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//send initial login packet containing name
			String name  = "George";
			serverOut.writeBytes(name+"\n");
			serverOut.flush();

			//receive ID number
			serverInput = serverIn.readLine();

			//set game's current player to a new player.
			game.setCurrentPlayer(name, Integer.parseInt(serverInput));

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

			//TODO create a method that sends a disconnect to the server

		}catch(Exception e){
			System.out.println("PROBLEM");
			e.printStackTrace();
		}
	}
}


