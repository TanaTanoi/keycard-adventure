package graphics;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author CJ Deighton
 *
 * Simple class to hold the normals vertices and texture coordinates of
 * a single face of an object file
 */
public class Face {
	public Vector3f vertex;
	public Vector3f normals;
	public Vector3f textures;

	/**
	 * @param v Vertices of face
	 * @param n Normals of face
	 * @param t Texture coordinates of face
	 */
	public Face(Vector3f v, Vector3f n, Vector3f t){
		vertex = v;
		normals = n;
		textures = t;
	}
}
