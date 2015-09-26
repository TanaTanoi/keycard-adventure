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
import org.lwjgl.util.vector.Vector3f;


public class Floor {

	int objectDisplayList;
	private char[][] room;
	private double spacing;
	private double gameSize = 10;
	private float x,y,z;
	private boolean loaded = false;
	public Floor(){
		setToBlank(26);
	}



	public void renderRoom(){
		if (!loaded){
			loadModel("sphere.obj");
			initaliseLighting();
		}
		renderObject();
		renderWalls();
		renderGrid();
	}

	private void loadModel(String filePath){
		loaded = true;
		Model m = new Model(filePath);				

		objectDisplayList = glGenLists(1);
		glNewList(objectDisplayList, GL_COMPILE);
		glBegin(GL_TRIANGLES);

		for(Face face: m.getFaces()){
			Vector3f n1 = m.getNormals().get((int) face.normals.x -1);
			glNormal3f(n1.x,n1.y,n1.z);
			Vector3f v1 = m.getVertices().get((int) face.vertex.x -1);
			glVertex3f(v1.x,v1.y,v1.z);

			Vector3f n2 = m.getNormals().get((int) face.normals.y -1);
			glNormal3f(n2.x,n2.y,n2.z);
			Vector3f v2 = m.getVertices().get((int) face.vertex.y -1);
			glVertex3f(v2.x,v2.y,v2.z);

			Vector3f n3 = m.getNormals().get((int) face.normals.z -1);
			glNormal3f(n3.x,n3.y,n3.z);
			Vector3f v3 = m.getVertices().get((int) face.vertex.z -1);
			glVertex3f(v3.x,v3.y,v3.z);	

			System.out.println(v3.x + " " + v3.y + " " + v3.z + " ");
		}
		glEnd();
		glEndList();
	}

	public void move(char pressed, double xRot){

		double dz = Math.cos(Math.toRadians(xRot))/20;
		double dx = Math.sin(Math.toRadians(xRot))/20;
		if (pressed == 'W'){
			z+=dz;
			x-=dx;
		}
		if (pressed == 'A'){
			z+=dx;
			x+=dz;
		}
		if (pressed == 'S'){
			z-=dz;
			x+=dx;
		}
		if (pressed == 'D'){
			z-=dx;
			x-=dz;
		}

	}

	private void renderObject(){
		glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);
		glColor3f(1f, 0.1f, 0);
		glPushMatrix();

		glTranslated(x, y+1, z);
		glScaled(0.1, 0.5, 0.1);
		glCallList(objectDisplayList);
		glPopMatrix();
	}

	private void renderGrid(){

		glBegin(GL_LINE);
		glColor3f(0.3f, 0.3f, 0.3f);
		for(double i = -room.length/2;i<=room.length/2;i++){
			glVertex3d(i*spacing+x,0,-(gameSize/2)+z);
			glVertex3d(i*spacing+x,0,(gameSize/2)+z);
			glVertex3d(-(gameSize/2)+x,0,i*spacing+z);
			glVertex3d((gameSize/2)+x,0,i*spacing+z);
		}
		glColor3f(1f,0,0);
		glVertex3d(0,0,0);
		glVertex3d(2,0,0);
		glColor3f(0,1f,0);
		glVertex3d(0,0,0);
		glVertex3d(0,2,0);
		glColor3f(0,0,1f);
		glVertex3d(0,0,0);
		glVertex3d(0,0,2);
		glEnd();
	}

	private void renderWalls(){
		glColor3f(1f,1f,0);
		for (int ix = 0; ix < room.length; ix++){
			for (int iz = 0; iz < room[0].length; iz++){
				glPushMatrix();
				glTranslated(x, y, z);
				if (room[ix][iz] == 'X'){
					//front and back
					glPushMatrix();
					glTranslated((ix-room.length/2)*spacing, 0, (iz-room.length/2)*spacing);
					RenderTools.fillRect(0, 0, spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-room.length/2)*spacing, 0, ((iz+1)-room.length/2)*spacing);
					//					glRectd(0,0,-spacing,2);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					//left and right
					glPushMatrix();
					glTranslated((ix-room.length/2)*spacing, 0, (iz-room.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					//					glRectd(0,0,-spacing,2);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();

					glPushMatrix();
					glTranslated(((ix+1)-room.length/2)*spacing, 0, (iz-room.length/2)*spacing);
					glRotated(90, 0, 1, 0);
					//					glRectd(0,0,-spacing,2);
					RenderTools.fillRect(0, 0, -spacing, 2);
					glPopMatrix();
				}
				glPopMatrix();
			}
		}
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

		//		glClearColor(0.5f, 0.5f, 0.5f, 0.0f); // sets background to grey
		//				glClearDepth(1.0f); // clear depth buffer
		//				glEnable(GL_DEPTH_TEST); // Enables depth testing
		//				glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth testing
		//				glMatrixMode(GL_PROJECTION); // sets the matrix mode to project

		//				float fovy = 45.0f;
		//				float aspect = 1f;
		//				float zNear = 0.1f;
		//				float zFar = 100.0f;
		//				GLU.gluPerspective(fovy, aspect, zNear, zFar);

		//				glMatrixMode(GL_MODELVIEW);

		//				glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 

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

	private FloatBuffer asFloatBuffer(float[] array){
		return (FloatBuffer)BufferUtils.createFloatBuffer(4).put(array).flip();
	}



}
