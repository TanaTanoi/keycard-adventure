package main;
//import basically everything
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import graphics.Face;
import graphics.Floor;
import graphics.Model;
import graphics.applicationWindow.Window;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import vec.*;
public class TestMain {
	/*glfw callback objects*/
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback   keyCallback;
	private GLFWMouseButtonCallback cbfun_m;
	private GLFWCursorPosCallback cbfun_c;
	private GLFWScrollCallback cbfun_s;

	/** Window **/
	Window window;

	/*Mouse control fields*/
	private Vector2 mousePos = new Vector2(0,0);
	private boolean mouse_down = false;

	/*Model transformation fields*/
	private float zoom = 1f;
	private float rot_x = 0;
	private float rot_y = 0;

	private Floor curFloor = new Floor();

	public static void main(String[] args) {
		new TestMain().run();

	}
	public void run() {
		System.out.println("Launching Test Window \nLWJGL :" + Sys.getVersion());
		try {
			//Initialize class
			init();
			//Start opengl main loop
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window.getID());
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWerrorfun
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Set up window here
		window = new Window();

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

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities(false); // valid for latest build
		//Clear the buffer to this frame
		glClearColor(0.9f, 0.9f, 0.9f, 1.0f);

		//Create a display list of the building



		/*The primary rendering loop! This is run until closed
		 * or the user presses escape (defined in KeyboardCallback)*/
		while ( glfwWindowShouldClose(window.getID()) == GL_FALSE ) {
			setUpCamera();
			//initialiseLighting();
			glClear(GL_COLOR_BUFFER_BIT); // clear the frame buffer
			/*#Insert methods that draw in here #*/
			glClear(GL_DEPTH_BUFFER_BIT);
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
			glShadeModel(GL_SMOOTH);
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);


			//renderGrid();
			curFloor.renderRoom();
			if (mousePos.y > 80){
				if (mousePos.x > 600){
					rot_x +=Math.pow((mousePos.x-600)/100,1.2);
				}
				else if (mousePos.x < 200){
					rot_x -=Math.pow((200-mousePos.x)/100,1.2);
				}
			}
			/*----------------------------------*/
			glFlush();
			glfwSwapBuffers(window.getID()); // swap the color buffers
			/*This polls for events that happened on the window
			 * (i.e. keyboard, mouse, scroll events)*/
			glfwPollEvents();
		}
	}
	/**
	 * Sets up the lighting in the scene, coming from the camera initially.
	 * This is called during initialize FIXME for some reason this removes all colour from the scene
	 */
	private void initialiseLighting(){
		float[] direction	  = {0.0f, 0.0f, 1.0f, 0.0f};
		float[] diffintensity = {0.7f, 0.7f, 0.7f, 1.0f};
		float[] ambient       = {0.2f, 0.2f, 0.2f, 1.0f};
		glLightfv(GL_LIGHT0, GL_POSITION,(FloatBuffer)BufferUtils.createFloatBuffer(4).put(direction).flip() );
		glLightfv(GL_LIGHT0, GL_DIFFUSE, (FloatBuffer)BufferUtils.createFloatBuffer(4).put(diffintensity).flip());
		glLightfv(GL_LIGHT0, GL_AMBIENT, (FloatBuffer)BufferUtils.createFloatBuffer(4).put(ambient).flip());
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHTING);
	}
	/**
	 * Sets up the camera and applies rotations from the mouse.
	 * This is called every draw tick
	 */
	private void setUpCamera(){
		/*glMatrixMode(GL_PROJECTION);
		glLoadIdentity();*/

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//glTranslatef(0,0,zoom);
		glScalef(zoom, zoom, zoom);
		glRotatef(rot_x, 0, 1, 0);
		glRotatef(rot_y, 1, 0, 0);


	}
	/**
	 * Renders a basic 2 by 2 grid along the x/z axis (y is up)
	 *  as well as length 2 axis from 0,0,0.
	 * Lighting is disabled for this grid (and enabled at the end).
	 */
	private void renderGrid(){
		glDisable(GL_LIGHTING);
		glMatrixMode(GL_MODELVIEW);
		glBegin(GL_LINES);
		glColor3f(0.3f, 0.3f, 0.3f);
		for(float i = -1;i<=1.1;i+=0.1){
			glVertex3f(i,0,-1);
			glVertex3f(i,0,1);
			glVertex3f(-1,0,i);
			glVertex3f(1,0,i);
		}
		glColor3f(1f,0,0);
		glVertex3f(0,0,0);
		glVertex3f(2,0,0);
		glColor3f(0,1f,0);
		glVertex3f(0,0,0);
		glVertex3f(0,2,0);
		glColor3f(0,0,1f);
		glVertex3f(0,0,0);
		glVertex3f(0,0,2);
		glEnd();
		glEnable(GL_LIGHTING);
	}

	private void renderCube(float x, float y, float z){
		glMatrixMode(GL_MODELVIEW);
		float w = 0.5f;
		glBegin(GL_QUADS);
		glColor3f(0.7f, 0.1f, 0.1f);
		glVertex3f(x+w, y+w, z+w);
		glVertex3f(x+w, y-w, z+w);
		glVertex3f(x-w, y-w, z+w);
		glVertex3f(x-w, y+w, z+w);
		glVertex3f(x+w, y+w, z-w);
		glVertex3f(x+w, y-w, z-w);
		glVertex3f(x-w, y-w, z-w);
		glVertex3f(x-w, y+w, z-w);
		glEnd();
	}
	private void KeyboardCallback(long window, int key, int scancode, int action, int mods){
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
			glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
		char pressed = (char)key;
		System.out.println("Key Pressed: " + pressed);
		curFloor.move(pressed, rot_x);


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
