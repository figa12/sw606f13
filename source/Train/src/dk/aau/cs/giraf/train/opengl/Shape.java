package dk.aau.cs.giraf.train.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * An abstract class used for drawing shapes.
 * 
 * @author jerian
 *
 */
public abstract class Shape {
    
    /** The width of the shape. */
    private float width = 0.0f;
    /** The height of the shape. */
    private float height = 0.0f;
    
    /** The buffer holding the vertices. */
    private FloatBuffer vertexBuffer;
    
    /** The initial vertex definition. */
    private float vertices[];
    
    /**
     * Initiates a {@link Shape} with specified size.
     * This class {@link Shape} implements an abstract method {@link #createVertices(float, float)} 
     * which is called during this constructor.
     * 
     * @param width  of the shape
     * @param height of the shape
     * @see #createVertices(float, float)
     */
    public Shape(float width, float height) {
        this.width = width;
        this.height = height;
        
        this.initialiseVertexBuffer();
    }
    
    /**
     * Creates the vertices based on the saved {@link #width} and {@link #height}.
     * Then initiates a {@link FloatBuffer} for the {@link #vertexBuffer} based on the {@link #vertices}.
     */
    private void initialiseVertexBuffer() {
        // Create the vertices based on the size of the shape
        this.vertices = this.createVertices(this.width, this.height);
        
        // Initiate the vertex buffer
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(this.vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        this.vertexBuffer = byteBuf.asFloatBuffer();
        this.vertexBuffer.put(this.vertices);
        this.vertexBuffer.position(0);
    }
    
    /**
     * An abstract method which must return the initial vertices forming the shape.
     * 
     * @param width
     * @param height
     * @return An array float[] of vertices that form a shape
     */
    protected abstract float[] createVertices(float width, float height);
    
    /**
     * Get this shape's vertex buffer.
     * @return {@link FloatBuffer} vertexBuffer
     */
    public FloatBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }
    
    /**
     * Get the vertices forming this shape.
     * @return float[] vertices
     */
    public float[] getVertices() {
        return this.vertices;
    }
    
    /**
     * Get the width.
     * @return float width
     */
    public float getWidth() {
        return this.width;
    }
    
    /**
     * Get the height.
     * @return float height
     */
    public float getHeight() {
        return this.height;
    }
    
    /**
     * Set the width of the shape.
     * @param width new value
     */
    protected void setWidth(float width) {
        this.width = width;
        this.initialiseVertexBuffer(); // recreate vertices
    }
    
    /**
     * Set the height of the shape.
     * @param height new value
     */
    protected void setHeight(float height) {
        this.height = height;
        this.initialiseVertexBuffer(); // recreate vertices
    }
    
    /**
     * Draws the shape with a standard drawing method.
     * This method calls {@link #draw(GL10, float, float, float, float)} and sets the color to white with no transparency.
     * This should be sufficient to draw squares and triangles,
     * the method should be overridden though.
     * 
     * @param gl the GL10 instance.
     * @see #draw(GL10 gl, float red, float green, float blue, float alpha)
     */
    public void draw(GL10 gl) {
        this.draw(gl, new Color()); // white, no tranparency
    }
    
    /**
     * Draws the shape with a standard drawing method, with the specified color.
     * This should be sufficient to draw squares and triangles,
     * the method should be overridden though.
     * 
     * @param gl    the GL10 instance.
     * @param red   a value between 0.0f and 1.0f.
     * @param green a value between 0.0f and 1.0f.
     * @param blue  a value between 0.0f and 1.0f.
     * @param alpha a value between 0.0f and 1.0f.
     * @see #draw(GL10)
     */
    public void draw(GL10 gl, Color color) {
        //Set the color of the shape
        gl.glColor4f(color.red, color.green, color.blue, color.alpha);
        
        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW); // TODO further investigation nedded
        
        //Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.getVertexBuffer());
        
        //Enable vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
        //Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.getVertices().length / 3);
        
        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
