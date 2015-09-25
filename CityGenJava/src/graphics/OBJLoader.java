package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {
	public static Model loadModel(String filePath)throws FileNotFoundException, IOException{
		File file = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model m = new Model();
		String line;
		while ((line = reader.readLine()) != null){
			if (line.startsWith("v ")) {
				String[] broken = line.split(" ");
				float x = Float.valueOf(Float.valueOf(broken[1]));
				float y = Float.valueOf(Float.valueOf(broken[2]));
				float z = Float.valueOf(Float.valueOf(broken[3]));
				m.vertices.add(new Vector3f(x,y,z));
			}
			else if (line.startsWith("vn ")) {	
				String[] broken = line.split(" ");
				float x = Float.valueOf(Float.valueOf(broken[1]));
				float y = Float.valueOf(Float.valueOf(broken[2]));
				float z = Float.valueOf(Float.valueOf(broken[3]));
				m.normals.add(new Vector3f(x,y,z));
			} 
			else if  (line.startsWith("f ")) {	
				String[] broken = line.split(" ");
				Vector3f vertexIndices = new Vector3f(
						Float.valueOf(broken[1].split("/")[0]),
						Float.valueOf(broken[2].split("/")[0]),
						Float.valueOf(broken[3].split("/")[0]));
				Vector3f normalIndices = new Vector3f(
						Float.valueOf(broken[1].split("/")[2]),
						Float.valueOf(broken[2].split("/")[2]),
						Float.valueOf(broken[3].split("/")[2]));
				m.faces.add(new Face(vertexIndices,normalIndices));
			}
		}
		reader.close();
		return m;
	}
}
