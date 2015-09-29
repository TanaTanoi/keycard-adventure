package graphics;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import controller.Game;


public class View {

	ArrayList<Integer> objectDisplayList;
	private char[][] room;
	private char[][] occupiedSpace;
	private double spacing;
	private double gameSize = 10;
	private float x,y,z;
	private boolean loaded = false;
	private int wallTexture;
	private int wallDisplayList;
	public View(){
		setToBlank(26);
		objectDisplayList = new ArrayList<Integer>();
		y = -0.75f;
		initaliseCollisionArray();
		System.out.println(occupiedSpace.length);

	}

	public View(Game g){
		// need to set listeners here
	}

	public void renderRoom(){
		if (!loaded){
			loadModel("box.obj", 0);
			loadModel("teapot.obj", 1);
			initaliseLighting();
			wallTexture = new Texture("brick.jpg").getTextureID();
		}
//		renderObject(0);
				renderWalls();
	}

	private void loadModel(String filePath, int out){
		loaded = true;
		Model m = new Model(filePath);				
		int[][] occupiesX = new int[occupiedSpace.length][2];
		for (int z = 0; z < occupiedSpace.length; z+=1){
			occupiesX[z][0] = Integer.MAX_VALUE;
			occupiesX[z][1] = Integer.MIN_VALUE;
		}	
		//		wallDisplayList = glGenLists(1);
		objectDisplayList.add(glGenLists(1));
		glNewList(objectDisplayList.get(out), GL_COMPILE);
		glBegin(GL_TRIANGLES);
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for(Face face: m.getFaces()){

			Vector2f t1 = m.getTextureCoordinates().get((int) face.textures.x -1);
			glTexCoord2d(t1.x,t1.y);
			Vector3f n1 = m.getNormals().get((int) face.normals.x -1);
			glNormal3f(n1.x,n1.y,n1.z);
			Vector3f v1 = m.getVertices().get((int) face.vertex.x -1);
			glVertex3f(v1.x,v1.y,v1.z);

			Vector2f t2 = m.getTextureCoordinates().get((int) face.textures.y -1);
			glTexCoord2d(t2.x,t2.y);
			Vector3f n2 = m.getNormals().get((int) face.normals.y -1);
			glNormal3f(n2.x,n2.y,n2.z);
			Vector3f v2 = m.getVertices().get((int) face.vertex.y -1);
			glVertex3f(v2.x,v2.y,v2.z);

			Vector2f t3 = m.getTextureCoordinates().get((int) face.textures.z -1);
			glTexCoord2d(t3.x,t3.y);
			Vector3f n3 = m.getNormals().get((int) face.normals.z -1);
			glNormal3f(n3.x,n3.y,n3.z);
			Vector3f v3 = m.getVertices().get((int) face.vertex.z -1);
			glVertex3f(v3.x,v3.y,v3.z);	
		}
		glEnd();
		glEndList();
	}

	public void move(char pressed, double xRot){

		double dz = Math.cos(Math.toRadians(xRot))/20;
		double dx = Math.sin(Math.toRadians(xRot))/20;

		if (!isOccupied(pressed,xRot)){
			if (pressed == 'W'){
				z+=dz;
				x-=dx;
			}
			else if (pressed == 'A'){
				z+=dx;
				x+=dz;
			}
			else if (pressed == 'S'){
				z-=dz;
				x+=dx;
			}
			else if (pressed == 'D'){
				z-=dx;
				x-=dz;
			}
		}
	}

	private boolean isOccupied(char pressed, double xRot){



		int sx = 0;
		int sz = 0;
		//		for (double range = 0.5; range < 2; range+=0.1){
		double dz = Math.cos(Math.toRadians(xRot));
		double dx = Math.sin(Math.toRadians(xRot));
		if (Math.abs(dz) < 0.2) dz*=4;
		if (Math.abs(dx) < 0.2) dx*=4;
		if (pressed == 'W'){
			sz = (int) ((z+dz)/0.1)+(occupiedSpace.length/2);
			sx = (int) ((x-dx)/0.1)+(occupiedSpace.length/2);

		}
		else if (pressed == 'A'){
			sz = (int) ((z+dz)/0.1)+(occupiedSpace.length/2);
			sx = (int) ((x+dx)/0.1)+(occupiedSpace.length/2);
		}
		else if (pressed == 'S'){
			sz = (int) ((z-dz)/0.1)+(occupiedSpace.length/2);
			sx = (int) ((x+dx)/0.1)+(occupiedSpace.length/2);
		}
		else if (pressed == 'D'){
			sz = (int) ((z-dz)/0.1)+(occupiedSpace.length/2);
			sx = (int) ((x-dx)/0.1)+(occupiedSpace.length/2);
		}

		//		}
		if (sx < 0) sx = 0;
		if (sz < 0) sz = 0;
		if (sx > occupiedSpace.length) sx = occupiedSpace.length-1;
		if (sz > occupiedSpace[0].length) sz = occupiedSpace.length-1;
		System.out.println(dz);
		return occupiedSpace[sx][sz] != 'O';
	}

	private void renderObject(int displayList){
		glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);
		glColor3f(1f, 1f, 1f);
		glDisable(GL_COLOR);
		glPushMatrix();
		glTranslated(x, y, z);
//		glScaled(0.2, 4, 0.2);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, wallTexture);
		glCallList(objectDisplayList.get(displayList));
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR);
		glPopMatrix();
	}
	
	private void renderWalls(){
		double sqSize = gameSize/occupiedSpace.length;
		glPushMatrix();
		glTranslated(x, y, z);
		for (int x = 0; x < occupiedSpace.length; x++){
			for (int z = 0; z < occupiedSpace[0].length; z++){
				if (occupiedSpace[x][z] == 'X'){
					glPushMatrix();
					
					glTranslated(x*sqSize-(gameSize/2), 0, z*sqSize-(gameSize/2));
					glScaled(sqSize/5, 2, sqSize/5);
					renderObject(0);
					
					glPopMatrix();
				}
			}
		}
		glPopMatrix();
	}

	private void initaliseCollisionArray(){
		occupiedSpace = new char[(int) (gameSize/0.5)][(int) (gameSize/0.5)];

		for (int x = 0; x < occupiedSpace.length; x++){
			for (int z = 0; z < occupiedSpace[0].length; z++){
				if(x == 0 || z == 0 || x == occupiedSpace.length-1 || z == occupiedSpace[0].length-1){
					occupiedSpace[x][z] = 'X';
				} 
				else occupiedSpace[x][z] = 'O';
			}
		}
		printOccupied();
	}

	private void setToBlank(int size){
		spacing = gameSize/size;
		room = new char[size][size];
		for (int x = 0; x < room.length; x++){
			for (int y = 0; y < room[0].length; y++){
				room[x][y] = 'O';
				if (x == 0 || x == room.length-1) room[x][y] = 'X';
				if (y == 0 || y == room.length-1) room[x][y] = 'X';
				//if (Math.random() < 0.01) room[x][y] = 'X';//TODO remove this tester code when done with textures
			}
		}
	}

	private void initaliseLighting() {

		glMatrixMode(GL_PROJECTION);

		glLoadIdentity();
		double near = 1; // near should be chosen as far into the scene as possible
		double far  = 100;
		double fov  = 0.5; // 1 gives you a 90° field of view. It's tan(fov_angle)/2.
		glFrustum(-near*fov, near*fov, -fov, fov, near, far); // sets perspective view
		//----------- Variables & method calls added for Lighting Test -----------//
		glShadeModel(GL_SMOOTH);
		glMaterialfv(GL_FRONT, GL_SPECULAR, asFloatBuffer(new float[]{0.5f,0.5f,0.5f,0.5f}));				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 0.1f);					// sets shininess

		glLightfv(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f,10f,0f,0f}));				// sets light position
		glLightfv(GL_LIGHT0, GL_SPECULAR, asFloatBuffer(new float[]{0.01f,0.01f,0.01f,0.01f}));				// sets specular light to white
		glLightfv(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{0.1f,0.1f,0.1f,0.5f}));					// sets diffuse light to white
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.5f,0.5f,0.5f,1f}));		// global ambient light 

		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0

		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
		//----------- END: Variables & method calls added for Lighting Test -----------/
	}

	private void printOccupied(){
		for (int x = 0; x < occupiedSpace.length; x++){
			for (int z = 0; z < occupiedSpace[0].length; z++){
				System.out.print(occupiedSpace[x][z]);
			}
			System.out.println();
		}
	}



	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}



}
