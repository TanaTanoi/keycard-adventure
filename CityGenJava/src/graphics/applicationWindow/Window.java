package graphics.applicationWindow;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	/*Window long*/
	private long window;

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

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Test Window", NULL, NULL);
		if ( window == NULL ){
			throw new RuntimeException("Failed to create the GLFW window");}
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
