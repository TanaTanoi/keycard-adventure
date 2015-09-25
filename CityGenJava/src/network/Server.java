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
		//Create list of clients
		List<ClientThread> clients = new ArrayList<ClientThread>();
		while(true){
			//Socket connectionSocket = clientSocket.accept();
			ClientThread ct = new ClientThread(clientSocket.accept());
			
			//Add client to list and start receiving client information
			System.out.println("Accepted client :" +ct.getName());
			ct.start();
			clients.add(ct);
		}
		
		
	}
	
}

class ClientThread extends Thread{
	Socket connection;
	String clInput;
	ClientThread(Socket connection){
		super();
		this.connection = connection;
	}
	
	public void run(){
		try{
			while(true){
				BufferedReader clientIn =new BufferedReader( 
						new InputStreamReader(connection.getInputStream()));
				
				DataOutputStream clientOut = new DataOutputStream(connection.getOutputStream());
				clInput = clientIn.readLine();
				System.out.println("Received :" + clInput+ ".");
				String output = "Received your input \n";
				clientOut.writeBytes(output);
			}
		}catch(Exception e){
			
		}finally{
		}
	}
	
}