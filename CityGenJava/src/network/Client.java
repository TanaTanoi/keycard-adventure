package network;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {

	public static final int port = 4444;
	
	public static void main(String args[]) throws Exception{
		
		String userIn;
		String serverInput;
		Scanner userInput = new Scanner(System.in);
		Socket clientSocket = new Socket("localhost",port);
		DataOutputStream serverOut = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		while(true){
			//send input
			System.out.print("Enter input: ");
			userIn = userInput.nextLine();
			serverOut.writeBytes(userIn+"\n");
			serverOut.flush();
			System.out.println("Sent");
			
			//receive input
			serverInput = serverIn.readLine();
			System.out.println("Received: " + serverInput);
			
		}
	}
	
	
}
