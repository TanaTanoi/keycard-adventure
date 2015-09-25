package graphics;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.*; 

public class Model {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Face> faces = new ArrayList<Face>();
}
