package controller;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import vec.Vector2;
import gameObjects.player.Player;
import gameObjects.world.GameWorld;
import graphics.View;
import graphics.applicationWindow.Window;

public class Game {
	
	GameWorld world; // Model
	Player current; // Player giving controls
	View view; // view
	
	//** GLFW Listeners **//
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback   keyCallback;
	private GLFWMouseButtonCallback cbfun_m;
	private GLFWCursorPosCallback cbfun_c;
	private GLFWScrollCallback cbfun_s;
	
	/*Mouse control fields*/
	private Vector2 mousePos = new Vector2(0,0);
	private boolean mouse_down = false;

	/*Model transformation fields*/
	private float zoom = 1f;
	private float rot_x = 0;
	private float rot_y = 0;
	
	public Game(String filename){
		world = new GameWorld(filename);
		view = new View(this);
		// need to make a spawn method for new player
	}	

	public static void main(String[] args) {
		new Game("gameWorld.txt");
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
		System.out.println("Key Pressed: " + pressed);
		view.move(pressed, rot_x);
	}

	private void MouseButtonCallback(long window, int button, int state, int arg3){
		System.out.println(button + " " + state  + " " + arg3);
		if(button == GLFW_MOUSE_BUTTON_1){
			mouse_down = state==1;
		}
	}
	private void MouseMotionCallback(long window, double xpos, double ypos) {
		if(mouse_down){
			rot_x +=0.3*(xpos-mousePos.x);
			//			rot_y +=0.3*(ypos-mousePos.y);
		}
		mousePos = new Vector2((float)xpos,(float)ypos);
	}
	/**
	 *
	 * @param window
	 * @param xoffset
	 * @param yoffset - Scroll amount (positive if forward/towards computer)
	 */
	private void ScrollCallback(long window, double xoffset, double yoffset) {
		if(yoffset>0){
			zoom*=1.1;
		}else{
			zoom/=1.1;
		}
	}

	

}
