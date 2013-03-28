package dk.aau.cs.giraf.train.opengl;

//import javax.microedition.khronos.opengles.GL10;

/**
 * The Square class extends {@link Shape}.
 * Initialise the Square with a size {@link #Square(float width, float height)}
 * The Square is drawn with the standard method {@link Shape#draw(GL10)}
 * 
 * @author Jesper
 * @see Shape
 * @see Shape#draw(GL10, float, float, float, float)
 */
public class Square extends Shape {
    
    /**
     * Creates a square with the specified size.
     * 
     * @param width 
     * @param height
     * @see Shape#Shape(float, float)
     */
	public Square(float width, float height) {
		super(width, height);
	}
	
	/**
	 * Creates vertices that generates a square.
	 * This method is called by the super class {@link Shape} in the constructor {@link Shape#Shape(float, float)}
	 * 
	 * @see Shape#createVertices(float, float)
	 * @see Shape#Shape(float, float)
	 * @return An array float[] of vertices that specify a square
	 */
	@Override
	protected float[] createVertices(float width, float height) {
        /* The order of the vertices also specifies whether the square
         * is facing towards or away from the perspective */
	    float vertices[] = {
                0.0f, 0.0f, 0.0f,       //Top Left
                width, 0.0f, 0.0f,      //Top Right
                0.0f, -height, 0.0f,    //Bottom Left
                width, -height, 0.0f    //Bottom Right
        };
        return vertices;
    }
}
