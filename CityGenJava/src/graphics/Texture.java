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

public class Texture {
	private int textureID;

	public Texture(String path){
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
	                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	            }
	        }

	        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

	        // You now have a ByteBuffer filled with the color data of each pixel.
	        // Now just create a texture ID and bind it. Then you can load it using
	        // whatever OpenGL method you want, for example:

	      int textureID = glGenTextures(); //Generate texture ID
	        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

	        //Setup wrap mode
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	        
	        //Setup texture scaling filtering
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	        //Send texel data to OpenGL
	        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, tex.getWidth(), tex.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	        //Return the texture ID so we can bind it later again
	      this.textureID = textureID;
		} catch (IOException e) {
			System.out.println("Invalid texture");
		}
	}

	public int getTextureID(){
		return textureID;
	}
}
