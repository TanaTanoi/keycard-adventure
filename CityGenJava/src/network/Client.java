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

	public void run() {
		try{

				String userIn;
				String serverInput;
				clientSocket = new Socket(gameHost,port);
				DataOutputStream serverOut = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				//send initial login packet containing name
				String name  = "testins";
				serverOut.writeBytes(name+"\n");
				serverOut.flush();
				//receive ID number
				serverInput = serverIn.readLine();
//				System.out.println("Received ID : " + serverInput);
				//set game's current player to a new player.
				game.setCurrentPlayer(name, Integer.parseInt(serverInput));

				//assume regular packet loop
			while(true){
				//send input containing player's information
				userIn = getPlayerOutput();
//				System.out.println("Sending " + userIn);
				serverOut.writeBytes(userIn+"\n");
				serverOut.flush();

				//receive input pertaining to the other players in the system
				serverInput = serverIn.readLine();
				decode(serverInput);
			}
		}catch(Exception e){
			System.out.println("PROBLEM");
			e.printStackTrace();
		}
	}


	/**
	 * Get the string to send to the server that represents the player's coordinates and ID
	 * The coordanites are multiplied by 100 and casted to ints for simple broadcasting.
	 * The server is expected to dividie it by 100 when received
	 * @return - String containing the current player's x,y and their ID
	 */
	public String getPlayerOutput(){
		//0:ID 1:X 2: y
		float[] info = game.getPlayerInfo();
		assert info.length == 3;
		info[1]*=100.0f;
		info[2]*=100.0f;
		String output = ((int)info[0]) + " " + ((int)info[1]) +  " " + ((int)info[2]);
		return output;
	}

	/**
	 * Decodes an input from the server and applies the changes to the local game
	 * @param input
	 */
	public void decode(String input){
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
				if(p[0]!=game.getPlayerInfo()[0]){
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
		game.updatePlayers(p);
	}



}


