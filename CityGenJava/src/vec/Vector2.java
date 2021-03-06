package vec;
/**
 * Basic vector object in 2D space. This object is immutable and all functions are pure.
 * @author Tana
 *
 */
public class Vector2 {
	public final float x,y;

	public Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}


	public Vector2 add(Vector2 ov){
		return new Vector2(x+ov.x,y+ov.y);
	}

	/**
	 * Subtracts the other vector from this vector component wise <br>
	 * <b> This vector - Other vector</b>
	 * @param ov - Vector to be subtracted from this vector.
	 * @return Result of the subtraction
	 */
	public Vector2 sub(Vector2 ov){
		return new Vector2(x-ov.x,y-ov.y);
	}
	/**
	 * Component wise multiplication
	 * @param ov
	 * @return
	 */
	public Vector2 mul(Vector2 ov){
		return new Vector2(x*ov.x,y*ov.y);
	}
	/**
	 * Returns this vector divided by the other vector<br>
	 *        <b>this/ oother vector</b>
	 * @param ov - other vector to divide
	 * @return Result of division
	 */
	public Vector2 div(Vector2 ov){
		return new Vector2(x/ov.x,y/ov.y);
	}
	/**
	 * Adds f to each component and returns it.
	 * @param f
	 * @return vector comprised of x + f and y + f
	 */
	public Vector2 add(float f){
		return new Vector2(x+f,y+f);
	}
	/**
	 * removes f from the vector
	 *
	 * @param f - Float to take from x and y
	 * @return vector that is made of x - f and y - f
	 */
	public Vector2 sub(float f){
		return new Vector2(x-f,y-f);
	}
	/**
	 * multiplies the vector components by f
	 * @param f
	 * @return
	 */
	public Vector2 mul(float f){
		return new Vector2(x*f,y*f);
	}
	/**
	 * Returns this vector divided by the input float
	 *      this/f
	 * @param f
	 * @return
	 */
	public Vector2 div(float f){
		return new Vector2(x/f,y/f);
	}

	/**
	 * Calculates the magnitude of the vector
	 * @return - the length of this vector
	 */
	public float magnitude(){
		return (float)Math.sqrt(x * x + y * y);
	}
	/**
	 * Returns this vector altered to get a magnitude of one.
	 * @return - New vector that is the normal of this vector
	 */
	public Vector2 unitVector(){
		float mag = (float)Math.sqrt(x * x + y * y);
		return new Vector2(x / mag, y / mag);
	}
	/**
	 * Dot product of this and the other.
	 * @param ov
	 * @return
	 */
	public float dot(Vector2 ov){
		return x * ov.x + y * ov.y;
	}
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Vector2))throw new IllegalArgumentException();
		Vector2 ov = (Vector2)o;
		boolean a = Math.round(x*1000)==Math.round(ov.x*1000);	// \
		boolean b = Math.round(y*1000)==Math.round(ov.y*1000);	//  |--If they are the same within 0.001 accuracy
		return a&&b;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Vec: (").append(x).append(",").append(y).append(")");
		return sb.toString();
	}

	@Override
	public int hashCode(){
		return Integer.parseInt(((int)x*100)+((int)y*100)+"");// I guess this works for now?
	}
}
