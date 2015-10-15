package Main;

import controller.ClientController;
import network.Server;

public class Main {

	public static void main(String[] args) throws Exception {

		if(args.length==1){
			System.out.println("Creating server");
			new Server(args[0]);
			return;
		}else if(args.length == 2){
			System.out.println("Creating client");
			new ClientController(args[0], args[1]);
			return;
		}else{
			System.out.println("Not enough arguments! Expected [filepath] [*IP if client]");
			return;
		}
	}

}
