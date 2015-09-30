package graphics.applicationWindow;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;
import graphics.View;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Window {

	/*Window long*/
	private long window;


	private GLFWErrorCallback errorCallback;


	/*Window properties*/
	private static int WIDTH = 800;
	private static int HEIGHT = 800;

	public Window(){
		init();
	}

	/**
	 * Init creates the window using GLFW.
	 * It sets it to hide until it is created
	 * and to resizable as well as determining the starting
	 * height and width of the application window
	 */
	public void init(){
		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Keycard Adventure", NULL, NULL);
		if ( window == NULL ){
			throw new RuntimeException("Failed to create the GLFW window");}

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window relative to primary monitor
		glfwSetWindowPos(
				getID(),
				(GLFWvidmode.width(vidmode) - getWidth()) / 2,
				(GLFWvidmode.height(vidmode) - getHeight()) / 2
				);

		// Make the OpenGL context current
		glfwMakeContextCurrent(getID());
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(getID());
		//Create a context from current (required and important)
		GLContext.createFromCurrent();
	}

	public long getID() {
		return window;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}


}
