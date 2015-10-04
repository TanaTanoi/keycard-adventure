package network;
import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Server {

	public static final int port = 4444;
	static GameWorld world;//FIXME Find a way to have this private. Its not great having it package level
	public static void main(String argv[]) throws Exception{

		//initialise client on this port
		ServerSocket clientInteractSocket = new ServerSocket(port);
		//Set up thread that controls client-server relations
		ClientThread ct = new ClientThread();
		ct.start();
		world = new GameWorld("fake.txt");
		//Constantly accept clients
		while(true){
			//LOGIN PROTOCOL -> Accept user -> take name from user -> send new ID to user -> add user to list
			//acept a client
			Socket cl = clientInteractSocket.accept();
			System.out.println("Accepted client :" +ct.getName());
			//Accept the name and make a new player for it
			BufferedReader clientIn =new BufferedReader(
								new InputStreamReader(cl.getInputStream()));
			String clInput = clientIn.readLine();

			//Add player to thee game world
			int pID = world.addNewPlayer(clInput);

			//Return an ID associated with this player.
			DataOutputStream clientOut = new DataOutputStream(cl.getOutputStream());
			clientOut.writeBytes(pID+"\n");
			//System.out.println("Sending " + pID);

			//Accept a client and add it to the
			ct.add(cl);
		}


	}

}

class ClientThread extends Thread{
	List<Socket> connections;
	String clInput;
	ClientThread(){
		super();
		this.connections = new ArrayList<Socket>();
	}

	public void run(){
		try{
			while(true){
				Thread.sleep(100);
				while(!connections.isEmpty()){
					System.out.println("Reading inputs");
					//get the input from each client
					for(int i =0;i<connections.size();i++){
						BufferedReader clientIn =new BufferedReader(
								new InputStreamReader(connections.get(i).getInputStream()));
						clInput = clientIn.readLine();
						NetworkDecoder.decodeClientInput(Server.world,clInput,i);
					}

					//Generate ouput to send to all clients
					String output = NetworkDecoder.prepPackage(Server.world);
					//Send output to all clients
					//System.out.println("Sending " + output);
					for(int i = 0; i < connections.size();i++){
						DataOutputStream clientOut = new DataOutputStream(connections.get(i).getOutputStream());
						clientOut.writeBytes(output+"\n");

					}
				}
			}
		}catch(Exception e){

		}finally{
		}
	}

	public void add(Socket newClient){
		connections.add(newClient);
	}


}