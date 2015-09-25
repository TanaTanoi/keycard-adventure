package graphics;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderTools {
	public static void fillRect(double x, double y, double width, double height){
		glBegin(GL_QUADS);	//Set mode to fill spaces within vertices

		glVertex3d(x,0f,y);
		glVertex3d(x+width,0f,y);
		glVertex3d(x+width,height,y);
		glVertex3d(x,height,y);

		glEnd();//End quad mode
	}


}
