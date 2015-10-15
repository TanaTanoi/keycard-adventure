package test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		Set<String> empty = new HashSet<String>();
		String input = NetworkDecoder.prepPackage(g,empty);
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
		Set<String> empty = new HashSet<String>();
		setupGame();
		/*Check that previous test holds*/
		String input = NetworkDecoder.prepPackage(g,empty);
		assertTrue("Moves aren't right, response was :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
		/*Then send and change the game world via new command from a player*/
		String clientInput = "P 0 1500 1000 0 ";
		assertTrue(NetworkDecoder.decodeClientInput(g, clientInput, 0,empty));
		/*Check that the updated package is true*/
		input = NetworkDecoder.prepPackage(g,empty);
		assertTrue("With two players, should be [] but is :" + input+":", input.equalsIgnoreCase("P 0 1500 1000 0 P 1 2000 2000 0 "));
		float[][] playerInfos =  {{0.0f, 15.0f,10.0f, 0.0f},
								{1.0f, 20.0f,20.0f, 0.0f}};
		checkGameworld_Positions(playerInfos);
	}

	/*Test if an interaction command is correctly broadcasted*/
	@Test
	public void test_network_decoder_interact_01(){
		Set<String> interact = new HashSet<String>();

		setupGame();
		/*Check that previous test holds*/
		String input = NetworkDecoder.prepPackage(g,interact);
		assertTrue("Moves aren't right, response was :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
		/*Then send and change the game world via new command from a player*/
		/*Check that the updated package is unchanged apart from the item call*/
		interact.add("ITEM 1 1");
		input = NetworkDecoder.prepPackage(g,interact);
		assertTrue("With two players, should be [] but is :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 ITEM 1 1 "));
		float[][] playerInfos =  {{0.0f, 10.0f,10.0f, 0.0f},
								{1.0f, 20.0f,20.0f, 0.0f}};
		checkGameworld_Positions(playerInfos);

		/*Check that extra interaction only applies to a single package*/
		input = NetworkDecoder.prepPackage(g,interact);
		assertTrue("Moves aren't right, response was :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
	}

	/*Test if a disconenction message can be created and decoded*/
	@Test
	public void test_network_decoder_disconect_01(){
		Set<String> interact = new HashSet<String>();

		setupGame();

		//Send a disconnect message from player 0. Should return false to show that they have disconnected
		assertFalse(NetworkDecoder.decodeClientInput(g, "DISC 0", 0, interact));

		String input = NetworkDecoder.prepPackage(g,interact);
		assertTrue("With two players, should be [] but is :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 ITEM 1 1 "));
		float[][] playerInfos =  {{0.0f, 10.0f,10.0f, 0.0f},
								{1.0f, 20.0f,20.0f, 0.0f}};
		checkGameworld_Positions(playerInfos);

		/*Check that extra interaction only applies to a single package*/
		input = NetworkDecoder.prepPackage(g,interact);
		assertTrue("Moves aren't right, response was :" + input+":", input.equalsIgnoreCase("P 0 1000 1000 0 P 1 2000 2000 0 "));
	}

	/*---------------*\
	 * Helper methods*
	\* --------------*/

	public void setupGame(){
		g = new GameWorld();
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
