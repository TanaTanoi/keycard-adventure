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
	
	
}
