package graphics;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;
import graphics.applicationWindow.Window;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;




public class View {

	ArrayList<Integer> objectDisplayLists;
	ArrayList<Integer> objectTextureList;
	private char[][] occupiedSpace;
	private GameWorld world;
	//	private char[][] occupiedSpace;
	private double spacing;
	private double gameSize = 20;
	private double squareSize = 0.5;
	private float x,y,z;
	private boolean loaded = false;
	private int wallTexture;
	private int wallDisplayList;
	private Player player;
	private Window w;
	private GLFWErrorCallback errorCallback;
	private double yChange = 0.003;
	private double playersY = 0.5;

	public View(GameWorld g){
		world = g;

		objectDisplayLists = new ArrayList<Integer>();
		objectTextureList = new ArrayList<Integer>();
		initaliseCollisions(100,100);
		y = -0.95f;
		w = new Window();
		player = g.getCurrentPlayer();

		player.move(0, -5);
	}

	public void renderView(){
		if (!loaded){
			loadModel("teapot.obj", 0);
			objectTextureList.add(new Texture("brick.jpg").getTextureID());
			initaliseCamera();
		}

		Location playerLoc = player.getLocation();
		x = playerLoc.getX();
		z = playerLoc.getY();
		renderObject(0);
		renderPlayers();
		renderWalls();
	}

	private void initaliseCollisions(int width, int depth) {
		occupiedSpace = new char[width][depth];
		for (int x = 0; x < width; x++){
			for(int z = 0; z < depth; z++){
				occupiedSpace[x][z] = 'O';
				if (x == 0 || x == width-1) occupiedSpace[x][z] = 'X';
				else if (z == 0 || z == depth-1) occupiedSpace[x][z] = 'X';
			}
		}
		squareSize = gameSize/width;
	}

	private void printCollisions(){
		for (int x = 0; x < occupiedSpace.length; x++){
			for(int z = 0; z < occupiedSpace[0].length; z++){
				//System.out.print(occupiedSpace[x][z]);
			}
			//System.out.println();
		}
	}

	public Window getWindow(){
		return w;
	}



	private void loadModel(String filePath, int out){

		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int[][] zValues = new int[100][2];
		loaded = true;
		Model m = new Model(filePath);
		objectDisplayLists.add(glGenLists(1));
		glNewList(objectDisplayLists.get(out), GL_COMPILE);
		glBegin(GL_TRIANGLES);
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

			maxX = Math.max(maxX, (int)((v3.x/squareSize)+50));
			minX = Math.min(minX, (int)((v3.x/squareSize)+50));
			if (zValues[(int)((v3.x/squareSize)+50)][0] == 0){
				zValues[(int)((v3.x/squareSize)+50)][0] = (int)((v3.z/squareSize)+50);
				zValues[(int)((v3.x/squareSize)+50)][1] = (int)((v3.z/squareSize)+50);
			}
			zValues[(int)((v3.x/squareSize)+50)][0] = Math.min((int)((v3.z/squareSize)+50),zValues[(int)((v3.x/squareSize)+50)][0]);
			zValues[(int)((v3.x/squareSize)+50)][1] = Math.max((int)((v3.z/squareSize)+50),zValues[(int)((v3.x/squareSize)+50)][1]);
			occupiedSpace[(int)((v3.x/squareSize)+50)][(int)((v3.z/squareSize)+50)] = 'T';
		}
		for (int x = minX; x < maxX;x++){
			for (int z = zValues[x][0]; z < zValues[x][1];z++){
				occupiedSpace[x][z] = 'T';
			}
		}

		glEnd();
		glEndList();


		printCollisions();
		//System.out.println("min = " + minX + " max = " + maxX);
	}

	public void move(char pressed, double xRot){
		double dz = Math.cos(Math.toRadians(xRot))/10;
		double dx = Math.sin(Math.toRadians(xRot))/10;
		if (pressed == 'W'){
			z+=dz;
			x-=dx;
		}
		else if (pressed == 'A'){
			z+=dx;
			x+=dz;
		}
		else if (pressed == 'S'){
			z-=dz;
			x+=dx;
		}
		else if (pressed == 'D'){
			z-=dx;
			x-=dz;
		}
		for (int j = -1; j < 2; j++){
			for (int i = -1; i < 2; i++){
				int x = (int)((this.x)/squareSize)+49+i;
				int z = (int)((this.z)/squareSize)+49+j;
				if (x < 0 || x >= occupiedSpace.length) return;
				if (z < 0 || z >= occupiedSpace[0].length) return;
				if (occupiedSpace[x][z] != 'O')return;
			}
		}

		player.move(this.x, this.z);
		world.getCurrentPlayer().move(this.x, this.z);
	}



	private void renderObject(int displayList){
		glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);
		//		glColor3f(0, 0, 1f);
		//		glDisable(GL_COLOR);
		glPushMatrix();
		glTranslated(x, y, z);
		//				glEnable(GL_TEXTURE_2D);
		//				glBindTexture(GL_TEXTURE_2D, wallTexture);
		glCallList(objectDisplayLists.get(displayList));
		//		glDisable(GL_TEXTURE_2D);
		//		glEnable(GL_COLOR);
		glPopMatrix();
	}

	private void renderPlayers(){
		List<Player> players = world.getPlayers();
		if (playersY > 1 ||playersY < 0.3){
			yChange*=-1;
			playersY+=yChange;
		}

		playersY+=yChange;
		int i = 0;
		for(Player p: players){
			//System.out.println(playersY+ " " + yChange);
			if (!p.equals(world.getCurrentPlayer())) {

				Location playerLoc = p.getLocation();
				//System.out.println(i + " "+ playerLoc.getX() + " " +  playerLoc.getY());
				glPushMatrix();
				i = p.getID();
				if (i == 0) glColor3f(1f,0,0);
				else if (i == 1) glColor3f(0,1f,0);
				else if (i == 2) glColor3f(1f,1f,0);
				else if (i == 3) glColor3f(0,0,1f);

				glTranslated(x+playerLoc.getX(), y+playersY, z+playerLoc.getY());
				glScaled(0.1, 0.1, 0.1);
				renderObject(0);
				glColor3f(1, 1, 1);
				glPopMatrix();
			}
		}
	}

	private void renderWalls(){
		//		glColor3f(1f,1f,0);

		//		glColor3f(1f, 0, 0);
		glDisable(GL_COLOR);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, objectTextureList.get(0));
		double spacing = gameSize/occupiedSpace.length;
		for (int ix = 0; ix < occupiedSpace.length; ix++){
			for (int iz = 0; iz < occupiedSpace[0].length; iz++){
				glPushMatrix();
				glTranslated(x, y, z);
				if (occupiedSpace[ix][iz] == 'X'){
					//front and back
					glPushMatrix();
					glTranslated((ix-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					RenderTools.fillRect(0, 0, spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, ((iz+1)-occupiedSpace.length/2)*spacing);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					//left and right
					glPushMatrix();
					glTranslated((ix-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();
				}
				glPopMatrix();
			}
		}
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR);

	}

	private void initaliseCamera() {
		glMatrixMode(GL_PROJECTION);

		glLoadIdentity();
		double near = 1; // near should be chosen as far into the scene as possible
		double far  = 100;
		double fov  = 0.5; // 1 gives you a 90° field of view. It's tan(fov_angle)/2.
		glFrustum(-near*fov, near*fov, -fov, fov, near, far); // sets perspective view


		//----------- Variables & method calls added for Lighting Test -----------//
		glShadeModel(GL_SMOOTH);
		glMaterialfv(GL_FRONT, GL_SPECULAR, asFloatBuffer(new float[]{0.5f,0.5f,0.5f,0.5f}));				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 0.1f);					// sets shininess

		glLightfv(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f,10f,0f,0f}));				// sets light position
		glLightfv(GL_LIGHT0, GL_SPECULAR, asFloatBuffer(new float[]{0.01f,0.01f,0.01f,0.01f}));				// sets specular light to white
		glLightfv(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{0.1f,0.1f,0.1f,0.5f}));					// sets diffuse light to white
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.5f,0.5f,0.5f,1f}));		// global ambient light

		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0

		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
	}

	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}
}
