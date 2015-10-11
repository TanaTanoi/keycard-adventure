package graphics;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import gameObjects.objects.Furniture;
import gameObjects.objects.Item;
import gameObjects.player.Player;
import gameObjects.world.Floor;
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

import controller.ClientController;


public class View {

	ArrayList<Integer> objectDisplayLists;
	ArrayList<Integer> objectTextureList;
	private char[][] occupiedSpace;
	private GameWorld world;
	private ClientController control;
	//	private char[][] occupiedSpace;
	private double spacing;
	private double gameSize = 20;
	private double squareSize = 0.5;
	private float x,y,z;
	private boolean loaded = false;
	private int wallTexture;
	private int wallDisplayList;
	private Window w;
	private GLFWErrorCallback errorCallback;
	private double yChange = 0.003;
	private float playersY = 0.5f;

	public View(GameWorld world,ClientController control){
		this.world = world;
		this.control = control;
		objectDisplayLists = new ArrayList<Integer>();
		objectTextureList = new ArrayList<Integer>();
		initaliseCollisions(100,100);
		y = -0.95f;
		w = new Window();
		//		teapot = new Furniture("teapot", "teapot.obj");
	}

	public void renderView(){
		if (!loaded){
			objectTextureList.add(new Texture("brick.jpg").getTextureID());
			printCollisions();
			loaded = true;
		}
		initaliseCamera();
		float delta = control.getRotation();
		glRotatef(delta, 0, 1, 0);
		if(control.getFloor()!=null){
			for(Item i: control.getFloor().getItems()){
				glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);
				glPushMatrix();
				glTranslated(x, y, z);
				glPushMatrix();
				Location l = i.getLocation();
				glTranslatef(l.getX(), y,l.getY());
				glScalef(0.1f, 0.1f, 0.1f);
				glCallList(control.getFloor().getDisplayList(i));
				glPopMatrix();
				glPopMatrix();
			}
		}
		Location playerLoc = control.getCurrentPlayer().getLocation();
		x = playerLoc.getX();
		z = playerLoc.getY();


		renderPlayers();
		renderWalls();

		renderDisplayBar();

	}

	private void renderDisplayBar() {
		glDisable(GL_DEPTH_TEST); //disable depth test so that objects don't draw over the display
		glDisable(GL_LIGHTING); //disable lighting so the bar isn't affected by shadows

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, w.getWidth(), 0, w.getHeight(), -1, 1);
		glMatrixMode(GL_MODELVIEW);

		drawInventory();
		drawMinimap();


		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		glColor3f(1,1,1);

	}

	private void drawInventory() {
		glBegin(GL_QUADS);
		//QUAD above mini map
		glVertex3f(5,230,0); 
		glVertex3f(5,215,0); 
		glVertex3f(215,215,0);
		glVertex3f(215,230,0);

		//LEFT of minimap
		glVertex3f(215,230,0);
		glVertex3f(215,10,0);
		glVertex3f(230,10,0);
		glVertex3f(230,230,0);

		//Bar along the bottom of the screen
		glVertex3f(230,50,0);
		glVertex3f(230,10,0);
		glVertex3f(550,10,0);
		glVertex3f(550,50,0);

		//Inventory box
		glVertex3f(550,180,0);
		glVertex3f(550,10,0);
		glVertex3f(790,10,0);
		glVertex3f(790,180,0);
		glEnd();

		glBegin(GL_TRIANGLES);//for smoothing out rough edges
		//triangle to the side of the mini-map
		glVertex3f(230,10,0);
		glVertex3f(250,10,0);
		glVertex3f(230,230,0);

		//triangle to the side of the inventory box
		glVertex3f(550,180,0);
		glVertex3f(550,10,0);
		glVertex3f(530,10,0);

		//triangle on the top of of the inventory box
		glVertex3f(550,180,0);
		glVertex3f(790,190,0);
		glVertex3f(790,180,0);


		glEnd();
	}

	private void drawMinimap(){
		List<Player> players = world.getPlayers();
		int size = 200;

		glBegin(GL_QUADS); //Draw white square for the background

		glVertex3f(10,10,0);
		glVertex3f(10,10+size,0);
		glVertex3f(10+size,10+size,0);
		glVertex3f(10+size,10,0);

		glEnd();

		float gridSpacing = size/occupiedSpace.length;


		glColor3f(0,0,0);
		glPointSize(2);
		glBegin(GL_POINTS);//draw the collision map (walls and objects)
		for (int x = occupiedSpace.length-1; x >= 0; x--){
			for (int z = 0; z < occupiedSpace[x].length; z++){
				if (occupiedSpace[x][z] != '-') glVertex3f(11+(x*gridSpacing),11+(z*gridSpacing),0);
			}			
		}
		glEnd();

		float playerSpacing = (float) (size/gameSize);
		for(Player p: players){ // draw all players as triangles
			if (!p.equals(control.getCurrentPlayer())) glColor3f(1,0,0); // make player blue if it isn't the current player
			else glColor3f(0,1,0); // otherwise green

			glPushMatrix();

			float x = size-(p.getLocation().getX()+10)*playerSpacing;
			float y = size-(p.getLocation().getY()+10)*playerSpacing;
			glTranslatef(11+x, 11+y, 0);
			glRotatef(p.getOrientation()-180,0, 0, 1);

			glBegin(GL_TRIANGLES);

			glVertex3f(0,playerSpacing/4,0);
			glVertex3f(-playerSpacing/2,-playerSpacing,0);
			glVertex3f(playerSpacing/2,-playerSpacing,0);

			glEnd();

			glPopMatrix();
		}

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
				System.out.print(occupiedSpace[x][z]);
			}
			System.out.println();
		}
	}

	public Window getWindow(){
		return w;
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
				if (occupiedSpace[100-x][100-z] != '-'){

					System.out.println(x + " " + z);
					return;
				}
			}
		}

		control.getCurrentPlayer().move(this.x, this.z);
	}



	private void renderObject(int displayList){
		glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);
		glPushMatrix();
		glTranslated(x, y, z);
		glCallList(objectDisplayLists.get(displayList));
		glPopMatrix();
	}

	private void renderPlayers(){
		double spacing = gameSize/occupiedSpace.length;
		List<Player> players = world.getPlayers();
		if (playersY > 1 ||playersY < 0.3){
			yChange*=-1;
			playersY+=yChange;
		}

		playersY+=yChange;
		int i = 0;
		for(Player p: players){
			if (!p.equals(control.getCurrentPlayer())) {
				Location playerLoc = p.getLocation();
				glPushMatrix();
				i = p.getID();
				if (i == 0) glColor3f(1f,0,0);
				else if (i == 1) glColor3f(0,1f,0);
				else if (p.equals(control.getCurrentPlayer())) glColor3f(1f,1f,0);
				else if (i == 3) glColor3f(0,0,1f);

				glTranslatef(x, y, z);

				glTranslatef(-playerLoc.getX(),playersY,-playerLoc.getY());

				//				System.out.println("rel: " + (x-playerLoc.getX()) + " z : " +  (z-playerLoc.getY()));
				//System.out.println("rot " + p.getOrientation());
				glRotated(p.getOrientation()+90,0,1,0); // -90 to make the spout point in the direction the player is facing
				glScaled(0.5, 0.5, 0.5);
				for(Item it : control.getFloor().getItems()){

					if (it.getModelName().contains("tea")){
						System.out.println("rendering player");
						glCallList(control.getFloor().getDisplayList(it));
						break;
					}
				}
				glColor3f(1, 1, 1);
				glPopMatrix();
				//				glPopMatrix();
			}
		}
	}

	private void renderWalls(){
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
					fillRect(0, 0, spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, ((iz+1)-occupiedSpace.length/2)*spacing);
					fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					//left and right
					glPushMatrix();
					glTranslated((ix-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					fillRect(0, 0, -spacing, 2);
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

	public void setMap(char[][] map){
		occupiedSpace = map;
	}

	public void fillRect(double x, double y, double width, double height){
		glBegin(GL_QUADS);	//Set mode to fill spaces within vertices
		float tw = (float) Math.abs(width);
		float th = (float) Math.abs(height);
		glTexCoord2f(0,th);
		glVertex3d(x,height,y);

		glTexCoord2f(0,0);
		glVertex3d(x,0f,y);

		glTexCoord2f(tw,0);
		glVertex3d(x+width,0f,y);

		glTexCoord2f(tw,th);
		glVertex3d(x+width,height,y);

		glEnd();//End quad mode
	}

	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}
}
