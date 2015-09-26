package network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
public class Server {

	public static final int port = 4444;
	
	public static void main(String argv[]) throws Exception{
		
		//initialise client on this port
		ServerSocket clientSocket = new ServerSocket(port);
		//Set up thread that controls client-server relations
		ClientThread ct = new ClientThread();
		ct.start();
		
		//Constantly accept clients
		while(true){
			//Accept a client and add it to the 
			ct.add(clientSocket.accept());
			//Add client to list and start receiving client information
			System.out.println("Accepted client :" +ct.getName());
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
				//get the input from each client
				for(int i =0;i<connections.size();i++){
					BufferedReader clientIn =new BufferedReader( 
							new InputStreamReader(connections.get(i).getInputStream()));
					clInput = clientIn.readLine();
					decode(clInput,i);
				}
				
				//Generate ouput to send to all clients
				String output = prepPackage();
				
				//Send output to all clients
				for(int i = 0; i < connections.size();i++){
					DataOutputStream clientOut = new DataOutputStream(connections.get(i).getOutputStream());
					clientOut.writeBytes(output+"\n");
					System.out.println("Sending " + output);
				}
			}
		}catch(Exception e){
			
		}finally{
		}
	}
	
	public void add(Socket newClient){
		connections.add(newClient);
	}
	
	/**
	 * Decode the input from player @player
	 * @param input - The direct input 
	 * @param player - The player who sent the input
	 */
	public void decode(String input, int player){
		System.out.println("Received "+ input + " from player " + player);
	}
	/**
	 * Prepares the output to be sent to all clients
	 * @return
	 */
	public String prepPackage(){
		return "-----";
	}
	
}