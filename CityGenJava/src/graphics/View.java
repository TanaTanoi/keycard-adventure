package graphics;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import gameObjects.objects.Entity;
import gameObjects.objects.Tool;
import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;
import graphics.applicationWindow.Window;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import controller.ClientController;

/**
 * @author CJ Deighton
 *
 * Class used for all rendering in the graphics pane
 *
 * Will render the floor of the client's current player
 */
public class View {
	private static final double ROOF_HEIGHT = 2.5;
	private char[][] occupiedSpace;
	private GameWorld world;
	private ClientController control;
	private double gameSize = 20;
	private double squareSize = 0.5;
	private float x,y,z;
	private boolean loaded = false;
	private Window w;
	private double yChange = 0.003;
	private float playersY = 0.5f;
	private float lightIntensity = 0.8f;
	private double inventoryAnimation;
	private double animationRate = 0.05;
	private boolean displayHud = true;
	private int text;
	float textFade = 0;

	private Map<String, Integer> texMap;

	public View(GameWorld world,ClientController control){
		this.world = world;
		this.control = control;
		initaliseCollisions(100,100);
		y = -0.95f;
		w = new Window();
	}


	/**
	 * Code called by the main render loop of the client controller.
	 * Calls all render methods required for the next buffer swap.
	 */
	public void renderView(){
		if (!loaded){
			loadTextures();
			printCollisions();
			loaded = true;
		}
		initaliseCamera();
		float delta = control.getRotation();
		glRotatef(delta, 0, 1, 0);

		if (control.getCurrentPlayer() != null){
			Location playerLoc = control.getCurrentPlayer().getLocation();
			x = playerLoc.getX();
			z = playerLoc.getY();

			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			renderObjects();
			renderPlayers();
			renderWalls();
			renderBounds();

			renderDisplayBar();

		}

		inventoryAnimation+=animationRate; //Allows for the sprite sheets in the inventory to change
		if (inventoryAnimation > 4 || inventoryAnimation < 0){
			animationRate*=-1;
			inventoryAnimation+=animationRate;
		}
	}

	/**
	 * Imports all textures that will be required in the game.
	 * These are loaded all at once rather than when the parser
	 * asks simply because there are so few of them
	 */
	private void loadTextures(){
		texMap= new HashMap<String,Integer>();
		texMap.put("brick.jpg", Texture.getTexture("brick.jpg"));
		texMap.put("wood.jpg", Texture.getTexture("wood.jpg"));

		texMap.put("red_potion.png", Texture.getTexture("red_potion.png"));
		texMap.put("rainbow_potion.png", Texture.getTexture("rainbow_potion.png"));

		texMap.put("knife.png", Texture.getTexture("knife.png"));
		texMap.put("syringe.png", Texture.getTexture("syringe.png"));
		texMap.put("crowbar.png", Texture.getTexture("crowbar.png"));

		texMap.put("purple_keycard.png", Texture.getTexture("purple_keycard.png"));
		texMap.put("red_keycard.png", Texture.getTexture("red_keycard.png"));
	}


	/**
	 * Displays all objects that are currently on the clients floor
	 */
	private void renderObjects(){
		glDisable(GL_TEXTURE_2D);
		if(control.getFloor()!=null){
			for(Entity i: control.getFloor().getEntities()){
				glPushMatrix();
				glTranslatef(x, y, z);
				glPushMatrix();
				Location l = i.getLocation();
				glTranslatef((float)(-l.getX()), 0,(float)(-l.getY()));
				glCallList(control.getFloor().getDisplayList(i));
				glPopMatrix();
				glPopMatrix();
			}
		}
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Draws the roof and floor of the current room
	 */
	private void renderBounds() {
		glColor3f(0.8f,0.8f,0.8f);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texMap.get("wood.jpg"));
		glPushMatrix();
		glTranslatef(x, y, z);
		glBegin(GL_QUADS);
		//Floor quad

		glTexCoord2f(0, 0);
		glVertex3d(-gameSize/2,0,-gameSize/2);
		glTexCoord2f(1, 0);
		glVertex3d(gameSize/2,0,-gameSize/2);
		glTexCoord2f(1, 1);
		glVertex3d(gameSize/2,0,gameSize/2);
		glTexCoord2f(0, 1);
		glVertex3d(-gameSize/2,0,gameSize/2);
		//Roof quad
		glTexCoord2f(0, 0);
		glVertex3d(-gameSize/2,ROOF_HEIGHT,-gameSize/2);
		glTexCoord2f(1, 0);
		glVertex3d(gameSize/2,ROOF_HEIGHT,-gameSize/2);
		glTexCoord2f(1, 1);
		glVertex3d(gameSize/2,ROOF_HEIGHT,gameSize/2);
		glTexCoord2f(0, 1);
		glVertex3d(-gameSize/2,ROOF_HEIGHT,gameSize/2);

		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}

	/**
	 * Calls the draw methods for the players HUD (Minimap, inventory,
	 * any text that is currently displayed, and the blue surround to these)
	 */
	private void renderDisplayBar() {
		glDisable(GL_DEPTH_TEST); //disable depth test so that objects don't draw over the display
		glDisable(GL_LIGHTING); //disable lighting so the bar isn't affected by shadows

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, w.getWidth(), 0, w.getHeight(), -1, 1);
		glMatrixMode(GL_MODELVIEW);

		drawInventory();
		if(control.getCurrentPlayer().canSeeMap()){
			drawMinimap();
		}
		drawHealth();
		displayText();


		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		glColor3f(1,1,1);

	}


	/**
	 * Shows any text from npcs or the game if there is any to display
	 */
	private void displayText() {
		glColor4f(1,1,1,textFade);
		if (textFade > 0) textFade -= 0.001;
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, text);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBegin(GL_QUADS);


		glTexCoord2f(0,0);
		glVertex3f(250, 230, 0);
		glTexCoord2f(0,1);
		glVertex3f(250, 70, 0);
		glTexCoord2f(1,1);
		glVertex3f(530, 70, 0);
		glTexCoord2f(1,0);
		glVertex3f(530, 230, 0);

		glEnd();
		glColor4f(0,0,0,textFade);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}

	/**
	 * Draws health bar portion of the HUD
	 */
	private void drawHealth() {

		double healthLost = (100 - control.getCurrentPlayer().getHealth())/100.0;
		int lostBar = (int)((520.0-260)*healthLost)+260;

		glBegin(GL_QUADS);
		//Draw full health bar
		glColor3f(0,1,0);
		glVertex3f(260,60,0);
		glVertex3f(260,45,0);
		glColor3f(1,0,0);
		glVertex3f(520,45,0);
		glVertex3f(520,60,0);


		//Lost Health Bar
		glColor3f(0.0f,0.23f,0.43f);
		glVertex3f(260,60,0);
		glVertex3f(260,45,0);
		glVertex3f(lostBar,45,0);
		glVertex3f(lostBar,60,0);

		glEnd();
	}

	/**
	 * Shows the blue surround to the players HUD
	 */
	private void drawInventory() {
		glColor3f(0.0f,0.12f,0.20f);

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
		glVertex3f(230,40,0);
		glVertex3f(230,10,0);
		glVertex3f(550,10,0);
		glVertex3f(550,40,0);

		//Health bar surround
		glVertex3f(255,63,0);
		glVertex3f(255,40,0);
		glVertex3f(525,40,0);
		glVertex3f(525,63,0);

		//Inventory box
		glVertex3f(550,180,0);
		glVertex3f(550,10,0);
		glVertex3f(790,10,0);
		glVertex3f(790,180,0);

		if (control.getCurrentPlayer().getEquipped() == 0) glColor3f(1,0,0);
		else glColor3f(0.0f,0.12f,0.20f);
		glVertex3f(557,173,0);
		glVertex3f(557,67,0);
		glVertex3f(663,67,0);
		glVertex3f(663,173,0);

		if (control.getCurrentPlayer().getEquipped() == 1) glColor3f(1,0,0);
		else glColor3f(0.0f,0.12f,0.20f);
		glVertex3f(667,123,0);
		glVertex3f(667,17,0);
		glVertex3f(773,17,0);
		glVertex3f(773,123,0);

		glColor3f(0.0f,0.23f,0.43f);

		glVertex3f(560,65,0);
		glVertex3f(560,60,0);
		glVertex3f(660,60,0);
		glVertex3f(660,65,0);

		glVertex3f(670,130,0);
		glVertex3f(670,125,0);
		glVertex3f(770,125,0);
		glVertex3f(770,130,0);

		glEnd();


		glColor3f(0.0f,0.23f,0.43f);
		glBegin(GL_TRIANGLES);//for smoothing out rough edges
		//triangle to the side of the mini-map
		glVertex3f(230,30,0);
		glVertex3f(250,10,0);
		glVertex3f(230,230,0);

		//triangle to the side of the inventory box
		glVertex3f(550,180,0);
		glVertex3f(550,30,0);
		glVertex3f(530,10,0);

		glEnd();

		glColor3f(1,1,1);

		renderItems();
	}

	/**
	 * Renders the items currently in the players inventory
	 */
	private void renderItems(){
		glEnable(GL_TEXTURE_2D);
		Tool[] inv = control.getCurrentPlayer().getInventory();
		if (inv[0] != null)	glBindTexture(GL_TEXTURE_2D, texMap.get(inv[0].getImagePath()));
		else glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);

		float frame = (int)inventoryAnimation;

		glTexCoord2f(frame/4, 0);
		glVertex3f(560,170,0);
		glTexCoord2f(frame/4, 1);
		glVertex3f(560,70,0);
		glTexCoord2f((frame+1)/4, 1);
		glVertex3f(660,70,0);
		glTexCoord2f((frame+1)/4, 0);
		glVertex3f(660,170,0);

		glEnd();
		glEnable(GL_TEXTURE_2D);
		if (inv[1] != null)	glBindTexture(GL_TEXTURE_2D, texMap.get(inv[1].getImagePath()));
		else glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glTexCoord2f(frame/4, 0);
		glVertex3f(670,120,0);
		glTexCoord2f(frame/4, 1);
		glVertex3f(670,20,0);
		glTexCoord2f((frame+1)/4, 1);
		glVertex3f(770,20,0);
		glTexCoord2f((frame+1)/4, 0);
		glVertex3f(770,120,0);
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
		if (!displayHud) return;
		float gridSpacing = size/occupiedSpace.length;


		glColor3f(0,0,0);
		glPointSize(2);
		glBegin(GL_POINTS);//draw the collision map (walls and objects)
		for (int x = 0; x < occupiedSpace.length; x++){
			for (int z = 0; z < occupiedSpace[x].length; z++){
				if (occupiedSpace[x][occupiedSpace[x].length-1-z] != '-') glVertex3f(11+(x*gridSpacing),11+(z*gridSpacing),0);
			}
		}
		glEnd();

		float playerSpacing = (float) (size/gameSize);
		for(Player p: players){ // draw all players as triangles
			if (!p.equals(control.getCurrentPlayer())) glColor3f(1,0,0); // make player blue if it isn't the current player
			else glColor3f(0,1,0); // otherwise green

			glPushMatrix();

			float x = size-(p.getLocation().getX()+10)*playerSpacing;
			float y = (p.getLocation().getY()+10)*playerSpacing;
			glTranslatef(11+x, 11+y, 0);
			glRotatef(-p.getOrientation(),0, 0, 1);

			glBegin(GL_TRIANGLES);

			glVertex3f(0,3*playerSpacing/4,0);
			glVertex3f(-playerSpacing/2,-3*playerSpacing/4,0);
			glVertex3f(playerSpacing/2,-3*playerSpacing/4,0);

			glEnd();

			glPopMatrix();
		}

	}

	/**
	 * Sets up the collision map with empty rooms and walls around the edges
	 * @param width
	 * @param depth
	 */
	private void initaliseCollisions(int width, int depth) {
		occupiedSpace = new char[width][depth];
		for (int x = 0; x < width; x++){
			for(int z = 0; z < depth; z++){
				occupiedSpace[x][z] = '-';
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

	/**
	 * Calculates where the player should land an then
	 * runs through the collision map to see if the player
	 * can move there, if so, it moves the player, else returns.
	 *
	 *
	 * @param pressed Key that the player pressed
	 * @param xRot Current orientation of the player
	 */
	public void move(char pressed, double xRot){
		double dz = Math.cos(Math.toRadians(xRot))/10;
		double dx = Math.sin(Math.toRadians(xRot))/10;
		float tempX = x;
		float tempZ = z;
		if (pressed == 'W'){
			tempZ+=dz;
			tempX-=dx;
		}
		else if (pressed == 'A'){
			tempZ+=dx;
			tempX+=dz;
		}
		else if (pressed == 'S'){
			tempZ-=dz;
			tempX+=dx;
		}
		else if (pressed == 'D'){
			tempZ-=dx;
			tempX-=dz;
		}
		for (int i = -13; i < 13; i++){
			int x = (int)(((tempX+10) + dx*i)/squareSize);
			int z = (int)(((tempZ+10) + dz*i)/squareSize);
			if (x <= 0 || x >= occupiedSpace.length) return;
			if (z <= 0 || z >= occupiedSpace[0].length) return;
			if (occupiedSpace[occupiedSpace.length-x][occupiedSpace[0].length-z] != '-') return;
		}
		control.getCurrentPlayer().move(tempX, tempZ);
	}

	/**
	 * returns the first character in the collision map that relates to an object
	 *
	 * @param xRot current orientation of the player
	 * @return closest character in the collision array in the direction the player is facing
	 */
	public char interact(double xRot){
		double dz = Math.cos(Math.toRadians(xRot))/10;
		double dx = Math.sin(Math.toRadians(xRot))/10;
		for (int i = -13; i < 13; i++){
			int x = (int)(((this.x-dx+10) + dx*i)/squareSize);
			int z = (int)(((this.z+dz+10) + dz*i)/squareSize);
			if (occupiedSpace[100-x][100-z] != '-'){
				return occupiedSpace[100-x][100-z];
			}
		}
		return '-';
	}

	private void renderPlayers(){
		glDisable(GL_TEXTURE_2D);
		double spacing = gameSize/occupiedSpace.length;
		List<Player> players = world.getPlayers();//control.getFloor().getPlayers();
		if (playersY > 1 ||playersY < 0.3){
			yChange*=-1;
			playersY+=yChange;
		}

		playersY+=yChange;
		int i = 0;
		for(Player p: players){
			if (!p.equals(control.getCurrentPlayer())&&
					p.getLocation().getFloor()==control.getCurrentPlayer().getLocation().getFloor()) {
				Location playerLoc = p.getLocation();
				glPushMatrix();
				i = p.getID();
				if (i == 0) glColor3f(1f,0,0);
				else if (i == 1) glColor3f(0,1f,0);
				else if (p.equals(control.getCurrentPlayer())) glColor3f(1f,1f,0);
				else if (i == 3) glColor3f(0,0,1f);

				glTranslatef(x, y, z);

				glTranslatef(-playerLoc.getX(),playersY,-playerLoc.getY());

				glRotated(p.getOrientation()+90,0,1,0); // -90 to make the spout point in the direction the player is facing
				glScaled(0.5, 0.5, 0.5);
				glCallList(control.getFloor().getPlayerDisplayList());

				glColor3f(1, 1, 1);
				glPopMatrix();
			}
		}
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * renders all X's in the collision map as walls
	 */
	private void renderWalls(){
		glColor3f(0.8f,0.8f,0.8f);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texMap.get("brick.jpg"));
		double spacing = gameSize/occupiedSpace.length;
		for (int ix = 0; ix < occupiedSpace.length; ix++){
			for (int iz = 0; iz < occupiedSpace[0].length; iz++){
				glPushMatrix();
				glTranslatef(x, y, z);
				if (occupiedSpace[ix][iz] == 'X'){
					//front and back
					glPushMatrix();
					glTranslated((ix-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					fillRect(0, 0, spacing, ROOF_HEIGHT);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, ((iz+1)-occupiedSpace.length/2)*spacing);
					fillRect(0, 0, -spacing, ROOF_HEIGHT);
					glPopMatrix();

					//left and right
					glPushMatrix();
					glTranslated((ix-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					fillRect(0, 0, -spacing, ROOF_HEIGHT);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-occupiedSpace.length/2)*spacing, 0, (iz-occupiedSpace.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					fillRect(0, 0, -spacing, ROOF_HEIGHT);
					glPopMatrix();
				}
				glPopMatrix();
			}
		}
		glDisable(GL_TEXTURE_2D);
	}


	/**
	 * Sets the players view frustum and the lighting
	 */
	private void initaliseCamera() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		double near = 1; // near should be chosen as far into the scene as possible
		double far  = 100;
		double fov  = 0.5; // 1 gives you a 90° field of view. It's tan(fov_angle)/2.
		glFrustum(-near*fov, near*fov, -fov, fov, near, far); // sets perspective view


		//----------- Variables & method calls added for Lighting Test -----------//
		glShadeModel(GL_SMOOTH);

		glLightfv(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0,5f,0,0.0f}));				// sets light position
		glLightfv(GL_LIGHT0, GL_SPECULAR, asFloatBuffer(new float[]{lightIntensity/3,lightIntensity/3,lightIntensity/3,0.01f}));				// sets specular light to white
		glLightfv(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{lightIntensity/3,lightIntensity/3,lightIntensity/3,0.5f}));					// sets diffuse light to white
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{lightIntensity,lightIntensity,lightIntensity,1f}));		// global ambient light

		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0

		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
	}

	/**
	 * Sets the collision map for the floor
	 * @param map
	 */
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

	/**
	 * Turns the detail of the HUD minimap (players, objects, and walls) on and off
	 */
	public void toggleHUD(){
		displayHud = !displayHud;
	}


	public float getLightIntensity() {
		return lightIntensity;
	}

	/**
	 * Allows the user to dim or brighten lights
	 * @param lightIntensity new brightness value
	 */
	public void setLightIntensity(float lightIntensity) {
		this.lightIntensity = lightIntensity;
	}

	/**
	 * Creates a float buffer from a given float array object
	 * @param array the array the buffer is created from
	 * @return a floatbuffer for 4 floats
	 */
	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}


	/**
	 * Loads in a texture of text to be displayed on the HUD
	 * @param text The file path of the texture if text to be output
	 */
	public void setText(String text) {
		textFade = 1;
		this.text = Texture.getTexture(text);
	}
}
