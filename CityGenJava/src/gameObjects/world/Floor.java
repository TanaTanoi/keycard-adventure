package gameObjects.world;

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glMaterialfv;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex3f;
import gameObjects.objects.Entity;
import gameObjects.objects.Item;
import gameObjects.objects.Portal;
import gameObjects.player.Character;
import gameObjects.player.NPC;
import gameObjects.player.Player;
import graphics.Face;
import graphics.Model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Floor {
	//Filepaths to Display lists
	private Map<String, Integer> displayLists;
	private static final float SQUARE_SIZE = 0.5f;
	private List<Player> players;
	private Map<Integer,Entity> entities;
	private int level;
	private char[][] floor;

	public Floor(int level, char[][] floorPlan, List<Entity> items){
		displayLists = new HashMap<String,Integer>();
		this.level = level;
		floor = floorPlan;
		this.players = new ArrayList<Player>();
		this.entities = new HashMap<Integer,Entity>();
		for(Entity i:items){
			this.addEntity(i);
		}

	}

	/**
	 * HELPER METHODS
	 * --------------
	 *
	 * The following methods are for adding and removing players
	 * as they enter or leave floors. They are also for adding or
	 * removing items from the floor as they are picked up or dropped
	 * by the players in the area
	 *
	 */
	public int getLevel(){
		return level;
	}

	public char[][] getFloorPlan(){
		return floor;
	}

	public List<Entity> getEntities(){
		List<Entity> listItems = new ArrayList<Entity>();
		listItems.addAll(entities.values());
		return listItems;
	}

	public List<Player> getPlayers(){
		return players;
	}

	public void addPlayer(Player c){
		players.add(c);
	}

	public void removePlayer(Character c){
		players.remove(c);
	}

	public void addEntity(Entity i){
		System.out.println("Adding item to floor " + i.getModelName());
		entities.put(i.getID(),i);
		System.out.println("Added item " + i.getID() + " " + i.getName());
		System.out.println(i.toString());
		Location l = i.getLocation();
		loadModel(i.getModelName(),new Vector3f(l.getX(),0,l.getY()));
	}

	public void removeEntity(Entity i){
		System.out.println(entities.remove(i.getID()));
	}

	public int getDisplayList(Entity i){
		return displayLists.get(i.getModelName());
	}

	private void loadModel(String filePath, Vector3f offset){

		Model m = new Model(filePath);
		updateCollisions(m, offset);
		int newList = glGenLists(1);
		glNewList(newList, GL_COMPILE);
		glBegin(GL_TRIANGLES);
		String materialSeed = ""+filePath.hashCode();
		while(materialSeed.length()<20){
			materialSeed = materialSeed+Math.abs(materialSeed.hashCode());
		}
		System.out.println("Mat len " + materialSeed.length());
		float[] values = new float[10];
		int len = materialSeed.length()-1;
		for(int i = 0; i < 10;i++){
			float a = Integer.parseInt(""+materialSeed.substring(i, i+2));
			float b = Integer.parseInt(""+materialSeed.substring(len-i-2, len-i));
			values[i] = Math.min(a/b,1.0f);
		}
		setMaterial(values);
		//TODO setMaterial(new float[]{});
		for(Face face: m.getFaces()){
			Vector2f t1 = m.getTextureCoordinates().get((int) face.textures.x -1);
			glTexCoord2d(t1.x,t1.y);
			Vector3f n1 = m.getNormals().get((int) face.normals.x -1);
			glNormal3f(n1.x,n1.y,n1.z);
			Vector3f v1 = m.getVertices().get((int) face.vertex.x -1);
			glVertex3f(v1.x,v1.y,v1.z);

			Vector2f t2 = m.getTextureCoordinates().get((int) face.textures.y -1);
			glTexCoord2d(t2.x,t2.y);
			Vector3f n2 = m.getNormals().get((int) face.normals.y -1);
			glNormal3f(n2.x,n2.y,n2.z);
			Vector3f v2 = m.getVertices().get((int) face.vertex.y -1);
			glVertex3f(v2.x,v2.y,v2.z);

			Vector2f t3 = m.getTextureCoordinates().get((int) face.textures.z -1);
			glTexCoord2d(t3.x,t3.y);
			Vector3f n3 = m.getNormals().get((int) face.normals.z -1);
			glNormal3f(n3.x,n3.y,n3.z);
			Vector3f v3 = m.getVertices().get((int) face.vertex.z -1);
			glVertex3f(v3.x,v3.y,v3.z);
		}

		glEnd();
		glEndList();
		displayLists.put(filePath,newList);
	}


	void setMaterial(float[] material){
		float[] mat = new float[4];

		// AMBIENT //
		mat[0] = material[0];//r
		mat[1] = material[1];//g
		mat[2] = material[2];//b
		mat[3] = 1.0f;
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, asFloatBuffer(mat));

		// DIFFUSE //
		mat = new float[4];
		mat[0] = material[3];//r
		mat[1] = material[4];//g
		mat[2] = material[5];//b
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, asFloatBuffer(mat));
		// SPECULAR //
		mat = new float[4];
		mat[0] = material[6];//r
		mat[1] = material[7];//g
		mat[2] = material[8];//b
		glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, asFloatBuffer(mat));
		glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material[9] * 128.0f);
	}

	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}

	private void updateCollisions(Model m, Vector3f offset){
		System.out.println("Offset " + offset.x + " " + offset.z);
		offset = new Vector3f(-offset.x*5,offset.y,-offset.z*5);
//		offset = new Vector3f(-offset.x,offset.y,-offset.z);
		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int[][] zValues = new int[100][2];
		int[][] map = new int[100][100];
		for(Face face: m.getFaces()){
			/*
//			Vector3f v3 = m.getVertices().get((int) face.vertex.z -1);
//			v3 = new Vector3f((v3.x+offset.x)/SQUARE_SIZE+50,(v3.y+offset.y)/SQUARE_SIZE+50,(v3.z+offset.z)/SQUARE_SIZE+50);
//			maxX = Math.max(maxX, (int)((v3.x)));
//			minX = Math.min(minX, (int)((v3.x)));
//
//			if (zValues[(int)((v3.x))][0] == 0){
//				zValues[(int)((v3.x ) )][0] = (int)((v3.z ) );
//				zValues[(int)((v3.x ) )][1] = (int)((v3.z ) );
//			}
//			zValues[(int)((v3.x ) )][0] = Math.min((int)((v3.z ) ),zValues[(int)((v3.x ) )][0]);
//			zValues[(int)((v3.x ) )][1] = Math.max((int)((v3.z ) ),zValues[(int)((v3.x ) )][1]);
//			floor[(int)((v3.x ) )][(int)((v3.z ) )] = 'T';
 * */
			Vector3f v3 = m.getVertices().get((int) face.vertex.z -1);
			v3 = new Vector3f(v3.x*SQUARE_SIZE,v3.y*SQUARE_SIZE,v3.z*SQUARE_SIZE);
			v3 = new Vector3f((v3.x)+offset.x,(v3.y)+offset.y,(v3.z)+offset.z);
			v3 = new Vector3f(v3.x+51,v3.y+51,v3.z+51);
		/*maxX = Math.max(maxX, (int)((v3.x)));
			minX = Math.min(minX, (int)((v3.x)));

			if (zValues[(int)((v3.x))][0] == 0){
				zValues[(int)((v3.x ) )][0] = (int)((v3.z ) );
				zValues[(int)((v3.x ) )][1] = (int)((v3.z ) );
			}
			zValues[(int)((v3.x ) )][0] = Math.min((int)((v3.z ) ),zValues[(int)((v3.x ) )][0]);
			zValues[(int)((v3.x ) )][1] = Math.max((int)((v3.z ) ),zValues[(int)((v3.x ) )][1]);
			floor[(int)((v3.x ) )][(int)((v3.z ) )] = 'T';*/
			map[(int) v3.x][(int) v3.z] = 1;
		}

//		for (int x = minX; x < maxX;x++){
//			for (int z = zValues[x][0]; z < zValues[x][1];z++){
//				floor[x][z] = 'T';
//			}
//		}
		for (int x = 0; x < 100;x++){
			for (int z = 0; z < 100;z++){
				if(map[x][z] == 1)
					floor[x][z] = 'T';
			}
	}

	}

	public Entity getEntity(int itemID) {
		System.out.println("SEGMESOIGMSEOGS");
		for(Entity e:entities.values()){
			System.out.println(e.toString());
		}System.out.println("------------");
		System.out.println("Attempting to access "+itemID);
		return entities.get(itemID);
	}

}
