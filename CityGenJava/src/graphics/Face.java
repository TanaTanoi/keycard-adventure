package graphics;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	public Vector3f vertex;
	public Vector3f normals;
	public Vector3f textures;
	
	public Face(Vector3f v, Vector3f n, Vector3f t){
		vertex = v;
		normals = n;
		textures = t;
	}
}
