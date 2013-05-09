package dk.aau.cs.giraf.train.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * An abstract class used for drawing shapes.
 * @author Jesper Riemer Andersen
 * @see Renderable
 */
public abstract class Shape extends Renderable {
    
    /** The width of the shape. */
    private float width;
    /** The height of the shape. */
    private float height;
    
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
    
    @Override
    public float getWidth() {
        return this.width;
    }
    
    @Override
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
     * The the size of the shape.
     * @param width  new value
     * @param height new value
     */
    protected void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.initialiseVertexBuffer(); // recreate vertices
    }
    
    /**
     * Draws the shape with a standard drawing method.
     * This method calls {@link #draw(GL10, Color)} and sets the color to white with no transparency.
     * This should be sufficient to draw squares and triangles,
     * the method should be overridden though.
     * 
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @see #draw(GL10 gl, Color color)
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
    
    /**
     * Draws the shape with a standard drawing method, with the specified color.
     * This should be sufficient to draw squares and triangles,
     * the method should be overridden though.
     * 
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color      the color overlay for this shape
     * @see #draw(GL10)
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate, Color color) {
        //Set the color of the shape
        gl.glColor4f(color.red, color.green, color.blue, color.alpha);
        
        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW);
        
        //Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.getVertexBuffer());
        
        //Enable vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
        //Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.getVertices().length / 3);
        
        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    
    /**
     * Rotate the shape around its center by the specified angle and draw it.
     * @param gl         the {@link GL10} instance.
     * @param coordinate where where the {@link Renderable} is being drawn.
     * @param angle      amount to rotate.
     */
    public void rotateCenterAndDraw(GL10 gl, Coordinate coordinate, float angle) {
        this.rotateCenterAndDraw(gl, coordinate, angle, Color.White);
    }
    
    /**
     * Rotate the shape around its center by the specified angle and draw it.
     * @param gl         the the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param angle      amount to rotate.
     * @param color      the color overlay for this shape.
     */
    public void rotateCenterAndDraw(GL10 gl, Coordinate coordinate, float angle, Color color) {
        //first move half width/height. This will be the center of the shape
        gl.glTranslatef(this.getWidth()/2, -this.getHeight()/2, 0f);
        
        //create a new drawing matrix at the shape's center
        gl.glPushMatrix();
        
        //then rotate the matrix
        gl.glRotatef(angle, 0f, 0f, 1f);
        
        //then move the shape, in the new matrix, to align the center of the shape with the center of the matrix
        gl.glTranslatef(-this.getWidth()/2, this.getHeight()/2, 0f);
        
        //draw
        this.draw(gl, coordinate, color);
        
        //discard the matrix
        gl.glPopMatrix();
        
        //then move back to where the original matrix were
        gl.glTranslatef(-this.getWidth()/2, this.getHeight()/2, 0f);
    }
}
