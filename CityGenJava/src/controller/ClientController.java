package controller;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import vec.Vector2;
import gameObjects.objects.Entity;
import gameObjects.objects.Item;
import gameObjects.player.Player;
import gameObjects.world.Floor;
import gameObjects.world.GameWorld;
import gameObjects.world.Location;
import gameObjects.world.Parser;
import graphics.View;
import graphics.applicationWindow.ConnectionWindow;
import graphics.applicationWindow.Window;
import network.Client;

public class ClientController {

	GameWorld world; // Model
	Floor floor;
	Player current; // Player giving controls
	View view; // view
	private Client client;
	//** GLFW Listeners **//
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback   keyCallback;
	private GLFWMouseButtonCallback cbfun_m;
	private GLFWCursorPosCallback cbfun_c;
	private GLFWScrollCallback cbfun_s;

	/*Mouse control fields*/
	private Vector2 mousePos = new Vector2(0,0);
	private boolean mouse_down = false;

	private float xRot = 0;
	private float rot_y = 0;

	/*Local player game-related fields*/
	private Entity toInteract = null;

	public ClientController(String filename,String IP){

		world = new GameWorld();
		view = new View(world,this);
		Parser.parseWorld(filename,world);
		glClearColor(0f, 0f, 0f, 1.0f);
		String[] nameAndIP = {"",""};

		ConnectionWindow cw = new ConnectionWindow(nameAndIP);
		while(cw.isVisible()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cw.dispose();
		IP = nameAndIP[1];
		//IP = "130.195.6.195";
		System.out.println(IP);
		//wait until we have been accepted
		try{
			if(IP.equals("LOCAL")){
				client = new Client(this);
			}else{
				client = new Client(this,IP);
			}
		}catch(Exception e){
			System.out.println("Unable to connect!");
			e.printStackTrace();
		}
		start();
	}

	private void start(){

		view.setMap(world.getCollisions());
		init();
		// need to make a spawn method for new player
		GL.createCapabilities(false); // valid for latest build
		//Clear the buffer to this frame
		glClearColor(0.9f, 0.9f, 0.9f, 1.0f);

		while ( glfwWindowShouldClose(view.getWindow().getID()) == GL_FALSE ) {
			renderLoop();
		}
		client.disconenct();
	}

	private void renderLoop(){
		setUpCamera();

		glClear(GL_COLOR_BUFFER_BIT); // clear the frame buffer
		/*#Insert methods that draw in here #*/
		glClear(GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		//Render the current view
		view.renderView();
		if (mousePos.y > 80){
			if (mousePos.x > 600){
				xRot +=Math.pow((mousePos.x-600)/100,1.4);
			}
			else if (mousePos.x < 200){
				xRot -=Math.pow((200-mousePos.x)/100,1.4);
			}
			//xRot %= 360;
			current.setOrientation((int)xRot);
		}
		/*----------------------------------*/
		glFlush();
		glfwSwapBuffers(view.getWindow().getID()); // swap the color buffers
		/*This polls for events that happened on the window
		 * (i.e. keyboard, mouse, scroll events)*/
		glfwPollEvents();
	}

	private void setUpCamera(){
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//glRotatef(xRot, 0, 1, 0);
	}

	public float getRotation(){
		return xRot;
	}

	/**
	 * Returns the item that wants to be picked up.
	 * After this is called, sets the pointer on this object to null
	 * @return - The item that wants to be picked up
	 */
	public Entity getToInteract(){
		Entity toReturn = toInteract;
		toInteract = null;
		return toReturn;
	}

	public static void main(String[] args) {
		new ClientController("testConfig.txt",args[0]);
	}

	/**
	 * Sets current player
	 * @param name
	 * @param ID
	 */
	public void setCurrentPlayer(String name, int ID){
		current = new Player(name,ID);
		world.getFloor(current.getLocation().getFloor()).addPlayer(current);
		world.addPlayer(current);
	}

	public float[] getPlayerInfo(){
		Location loc = current.getLocation();
		return new float[]{current.getID(),loc.getX(),loc.getY(),current.getOrientation()};
	}
	public Player getCurrentPlayer(){
		return current;
	}

	/**
	 * Takes a series of player info arrays and tells the world to update the player's
	 * associated with an ID.
	 * If the ID isn't present, the player will be added.
	 * @param playerInfo
	 */
	public void updatePlayer(int ID, float x, float y, int rotation){
		//Ignore the call if it wants to change our current player
		if(current.getID()==ID){return;}
		List<Player> players = world.getPlayers();

		for(Player p: players){
			if(p.getID()==ID){
				p.move(x, y);
				p.setOrientation(rotation);
				return;
			}
		}
		//if we get to the end and we haven't found and modified the player
		//then it is a player we haven't got in the array, and thus need to add a new one
		//Make the player, apply their movements and rotation, then add to client
		System.out.println("Adding player " + ID);
		Player newP = new Player("Dave",ID);
		newP.move(x, y);
		newP.setOrientation(rotation);
		world.addPlayer(newP);
	}

	public Floor getFloor(){
		return world.getFloor(current.getLocation().getFloor());
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Set up window here
		Window window = view.getWindow();

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window.getID(), keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				KeyboardCallback(window,key,scancode,action,mods);
			}
		});

		glfwSetMouseButtonCallback(window.getID(), cbfun_m = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long arg0, int button, int state, int arg3) {
				MouseButtonCallback(arg0, button, state, arg3);
			}
		});

		glfwSetCursorPosCallback(window.getID(),cbfun_c = new GLFWCursorPosCallback(){
			public void invoke(long window, double xpos, double ypos) {
				MouseMotionCallback(window, xpos, ypos);

			};

		});
		glfwSetScrollCallback(window.getID(), cbfun_s = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				ScrollCallback(window,xoffset,yoffset);
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window relative to primary monitor
		glfwSetWindowPos(
				window.getID(),
				(GLFWvidmode.width(vidmode) - window.getWidth()) / 2,
				(GLFWvidmode.height(vidmode) - window.getHeight()) / 2
				);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window.getID());
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window.getID());
		//Create a context from current (required and important)
		GLContext.createFromCurrent();
	}



	private void KeyboardCallback(long window, int key, int scancode, int action, int mods){
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
			glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
		char pressed = (char)key;
		if (action == 0){
			if(pressed == 'Q') current.equipLeft();
			if(pressed == 'E') current.equipRight();
			if(pressed == 'L') view.setLightIntensity(view.getLightIntensity()+0.05f);
			if(pressed == 'K') view.setLightIntensity(view.getLightIntensity()-0.05f);
			if(pressed == 'M') view.toggleHUD();
		}
		view.move(pressed, xRot);
	}

	private void MouseButtonCallback(long window, int button, int state, int arg3){
		System.out.println(button + " " + state  + " " + arg3);
		if(button == GLFW_MOUSE_BUTTON_1){
			mouse_down = state==1;

			toInteract = world.closestEntity(current.getLocation(), 10.0f);//TODO calibrate pickup radius
		}
	}
	private void MouseMotionCallback(long window, double xpos, double ypos) {
		mousePos = new Vector2((float)xpos,(float)ypos);
	}
	/**
	 *
	 * @param window
	 * @param xoffset
	 * @param yoffset - Scroll amount (positive if forward/towards computer)
	 */
	private void ScrollCallback(long window, double xoffset, double yoffset) {
	}

	public void pickUp(int playerID, int itemID) {
		world.pickUpItem(playerID, itemID);
	}

	public void interact(int playerID, int interactID){
		world.interact(playerID, interactID);
	}



}
