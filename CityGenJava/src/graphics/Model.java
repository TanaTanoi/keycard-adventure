package graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.*;


/**
 * @author CJ Deighton
 *
 * Class used for loading and storing objects to later be
 * loaded into display lists
 */
public class Model {
	private List<Vector3f> vertices;
	private List<Vector3f> normals;
	private List<Vector2f> textureCoordinates;
	private List<Face> faces;

	public Model(String filePath){
		loadOBJModel(filePath);
	}

	private void loadOBJModel(String filePath){
		textureCoordinates = new ArrayList<Vector2f>();
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		faces = new ArrayList<Face>();
		try {
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null){
				if (line.startsWith("v ")) {
					String[] broken = line.split(" ");
					float x = Float.valueOf(Float.valueOf(broken[1]));
					float y = Float.valueOf(Float.valueOf(broken[2]));
					float z = Float.valueOf(Float.valueOf(broken[3]));
					vertices.add(new Vector3f(x,y,z));
				}
				else if (line.startsWith("vn ")) {
					String[] broken = line.split(" ");
					float x = Float.valueOf(Float.valueOf(broken[1]));
					float y = Float.valueOf(Float.valueOf(broken[2]));
					float z = Float.valueOf(Float.valueOf(broken[3]));
					normals.add(new Vector3f(x,y,z));
				}
				else if (line.startsWith("vt ")) {
					String[] broken = line.split(" ");
					float x = Float.valueOf(Float.valueOf(broken[1]));
					float y = Float.valueOf(Float.valueOf(broken[2]));
					textureCoordinates.add(new Vector2f(x,y));
				}
				else if  (line.startsWith("f ")) {
					String[] broken = line.split(" ");
					Vector3f vertexIndices = new Vector3f(
							Float.valueOf(broken[1].split("/")[0]),
							Float.valueOf(broken[2].split("/")[0]),
							Float.valueOf(broken[3].split("/")[0]));
					Vector3f textureIndices = new Vector3f(
							Float.valueOf(broken[1].split("/")[1]),
							Float.valueOf(broken[2].split("/")[1]),
							Float.valueOf(broken[3].split("/")[1]));
					Vector3f normalIndices = new Vector3f(
							Float.valueOf(broken[1].split("/")[2]),
							Float.valueOf(broken[2].split("/")[2]),
							Float.valueOf(broken[3].split("/")[2]));
					faces.add(new Face(vertexIndices,normalIndices,textureIndices));
				}
			}
			reader.close();
		} catch (NumberFormatException e) {
			System.out.println("Invalid file type");
		} catch (IOException e) {
			System.out.println("File not found");
		}
	}

	public List<Vector2f> getTextureCoordinates() {
		return textureCoordinates;
	}

	public List<Vector3f> getVertices() {
		return vertices;
	}

	public List<Vector3f> getNormals() {
		return normals;
	}


	public List<Face> getFaces() {
		return faces;
	}

}

