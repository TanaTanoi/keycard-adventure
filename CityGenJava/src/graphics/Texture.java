package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLUtil;

//import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.GL12;
import static org.lwjgl.opengl.GL11.*;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * @author CJ Deighton
 *
 * Class for loading textures from a file
 */
public class Texture {

	/**
	 * Loads a texture from a file and returns the integer
	 * corresponding to this
	 *
	 * @param path Filepath of texture
	 * @return integer of the texture create
	 */
	public static int getTexture(String path){
		try {
			BufferedImage tex = ImageIO.read(new File(path));
			int[] pixels = new int[tex.getWidth() * tex.getHeight()];
	        tex.getRGB(0, 0, tex.getWidth(), tex.getHeight(), pixels, 0, tex.getWidth());

	        ByteBuffer buffer = BufferUtils.createByteBuffer(tex.getWidth() * tex.getHeight() * 4); //4 for RGBA, 3 for RGB

	        for(int y = 0; y < tex.getHeight(); y++){
	            for(int x = 0; x < tex.getWidth(); x++){
	                int pixel = pixels[y * tex.getWidth() + x];
	                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	                buffer.put((byte) (pixel & 0xFF));               // Blue component
	                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component
	            }
	        }

	        buffer.flip();

	     	int textureID = glGenTextures(); //Generate texture ID
	        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);//repeat mode for continuous textures
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, tex.getWidth(), tex.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	      return textureID;
		} catch (IOException e) {
			System.out.println("Invalid texture");
		}
		return 0;
	}
}
