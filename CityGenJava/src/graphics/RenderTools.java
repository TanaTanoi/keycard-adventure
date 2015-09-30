package graphics;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderTools {
//	private static Texture wallTexture = new Texture("brick.jpg");


	public static void fillRect(double x, double y, double width, double height){
		glEnable(GL_TEXTURE_2D);
//		glBindTexture(GL_TEXTURE_2D, wallTexture.getTextureID());
		glBegin(GL_QUADS);	//Set mode to fill spaces within vertices

		glTexCoord2d(0,1);
		glVertex3d(x,height,y);

		glTexCoord2d(0,0);
		glVertex3d(x,0f,y);

		glTexCoord2d(1,0);
		glVertex3d(x+width,0f,y);

		glTexCoord2d(1,1);
		glVertex3d(x+width,height,y);

		glEnd();//End quad mode
	}
}
