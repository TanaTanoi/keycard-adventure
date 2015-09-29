package network;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import controller.Game;
public class Client {

	private  int port = 4444;
	
	public static void main(String args[]) throws Exception{
		
		
	}
	/**
	 * Standard constructor that uses default port (4444)
	 * @throws Exception - If unable to connect to the server via standard port (4444)
	 */
	public Client(Game game) throws Exception{
		String userIn;
		String serverInput;
		Scanner userInput = new Scanner(System.in);
		Socket clientSocket = new Socket("localhost",port);
		DataOutputStream serverOut = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		int i = 0;
		while(true){
			//send input
			userIn = ":"+i+":"; 
			serverOut.writeBytes(userIn+"\n");
			serverOut.flush();
			System.out.println("Sending " + userIn);
			//receive input
			serverInput = serverIn.readLine();
			System.out.println("Received: " + serverInput);
			i++;
		}
	}
	/**
	 * Constructor that takes a different port to standard.
	 * @param port - The port to connect through
	 * @throws Exception - If unable to connect to the server via given port
	 */
	public Client(Game game,int port) throws Exception{
		this.port = port;
		new Client(game);
	}
	
	/**
	 * Get the string to send to the server that represents the player's coordinates
	 * @return - String containing the current player's x,y
	 */
	public String getPlayerOutput(){
		return "example 10 14";
	}
	
	/**
	 * Decodes an input from the server and applies the changes to the local game
	 * @param input
	 */
	public void decode(String input){
		
	}
	
}
