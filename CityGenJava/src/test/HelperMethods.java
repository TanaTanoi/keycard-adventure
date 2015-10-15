package test;

import java.util.ArrayList;

import gameObjects.objects.Entity;
import gameObjects.objects.Weapon;
import gameObjects.player.Player;
import gameObjects.world.Floor;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;

public class HelperMethods {

	public static GameWorld setupGame(){
		GameWorld g = new GameWorld();
		Player p1 = new Player("dave",0);
		p1.move(10, 10);
		Player p2 = new Player("John",1);
		p2.move(20, 20);
		g.addPlayer(p1);
		g.addPlayer(p2);

		return g;
	}

	public static void createFloor(GameWorld g){
		g.addFloor(new Floor(1,new char[100][100], new ArrayList<Entity>()));
	}

	public static Weapon addWeapon(GameWorld g) {
		Weapon w = new Weapon("Knife", "A sharp knife", new Location(1,1,1), 5, "locker.obj", 0, "knife.png");
		g.getFloor(1).addEntity(w);
		return w;
	}

}
