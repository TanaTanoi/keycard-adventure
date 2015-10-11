package test;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import network.*;
public class NetworkPackageTests {

	
	GameWorld g;
	/*Test the game correctly sends the players positions*/
	@Test
	public void test_network_decoder_01(){
		setupGame();
		String input = NetworkDecoder.prepPackage(g);
		/*Check that the server outputs the correct information (x and y are times 100)*/
		assertTrue("With two players, should be [] but is :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
		/*Then double check that the world is actually updated to this (x and  y are normal values)*/
		float[][] playerInfos =  {{0.0f, 10.0f,10.0f, 0.0f},
							{1.0f, 20.0f,20.0f, 0.0f}};
		checkGameworld_Positions(playerInfos);
	}
	/*Test that the server correctly moves a player*/
	@Test
	public void test_network_decoder_move_01(){
		setupGame();
		/*Check that previous test holds*/
		String input = NetworkDecoder.prepPackage(g);
		assertTrue("Moves aren't right, response was :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
		/*Then send and change the game world via new command from a player*/
		String clientInput = "P 0 1500 1000 0 ";
		assertTrue(NetworkDecoder.decodeClientInput(g, clientInput, 0));
		/*Check that the updated package is true*/
		input = NetworkDecoder.prepPackage(g);
		assertTrue("With two players, should be [] but is :" + input+":", input.equalsIgnoreCase("P 0 1500 1000 0 P 1 2000 2000 0 "));
		float[][] playerInfos =  {{0.0f, 15.0f,10.0f, 0.0f},
								{1.0f, 20.0f,20.0f, 0.0f}};
		checkGameworld_Positions(playerInfos);
	}
	
	/*---------------*\
	 * Helper methods*
	\* --------------*/
	
	public void setupGame(){
		g = new GameWorld("floor01.txt");
		Player p1 = new Player("dave",0);
		p1.move(10, 10);
		Player p2 = new Player("John",1);
		p2.move(20, 20);
		g.addPlayer(p1);
		g.addPlayer(p2);
	}
	
	/*Check if the players information in the game world is the same as what is provided*/
	public void checkGameworld_Positions(float[][] players){
		List<float[]> infos = g.getPlayerInfos();
		for(int j =0;j<infos.size();j++){
			for(int i =0;i<4;i++){
				assertTrue("Info should be equal " +players[j][i] + " " + infos.get(j)[i],players[j][i] == infos.get(j)[i]);
			}
		}
	}
	
}