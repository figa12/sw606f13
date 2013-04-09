package dk.aau.cs.giraf.train.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A {@link Renderable} gradient square that extends {@link Square}.
 * Two colors can be specified to create gradiance with.
 * @author Jesper
 * @see Square
 */
public class GradientSquare extends Square {
    
    /** The buffer holding the color values. */
    private FloatBuffer colorBuffer;
    
    /** The styles of the color gradient. */
    public enum GradientStyle {
        Vertical, Horizontal
    }
    
    /**
     * Creates a gradient square with the specified size.
     * The gradient is vertical or horizontal.
     * The gradient color is {@code firstColor} to {@code secondColor} respectively top to bottom or left to right.
     * @param width
     * @param height
     * @param firstColor  gradient
     * @param secondColor gradient
     * @param style       the gradient style
     * @see Square#Square(float, float)
     * @see Color
     * @see GradientStyle
     */
    public GradientSquare(float width, float height, Color firstColor, Color secondColor, GradientStyle style) {
        super(width, height);
        this.initialiseColorBuffer(firstColor, secondColor, style);
    }
    
    /**
     * Map colors to the vertices.
     * @param firstColor
     * @param secondColor
     * @param style
     */
    private void initialiseColorBuffer(Color firstColor, Color secondColor, GradientStyle style) {
        
        float colors[];
        
        //Set the colors according to the style
        switch(style) {
        case Horizontal:
            colors = new float[] {
                    firstColor.red,  firstColor.green,  firstColor.blue,  firstColor.alpha,    //Top Left
                    secondColor.red, secondColor.green,  secondColor.blue,  secondColor.alpha, //Top Right
                    firstColor.red,  firstColor.green,  firstColor.blue,  firstColor.alpha,    // Bottom Left
                    secondColor.red, secondColor.green,  secondColor.blue,  secondColor.alpha  //Bottom Right
            };
            break;
        case Vertical:
            colors = new float[] {
                    firstColor.red,  firstColor.green,  firstColor.blue,  firstColor.alpha,    //Top Left
                    firstColor.red,  firstColor.green,  firstColor.blue,  firstColor.alpha,    //Top Right
                    secondColor.red, secondColor.green,  secondColor.blue,  secondColor.alpha, // Bottom Left
                    secondColor.red, secondColor.green,  secondColor.blue,  secondColor.alpha  //Bottom Right
            };
            break;
        default:
            return;
        }
        
        // Initialise the color buffer
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        this.colorBuffer = byteBuf.asFloatBuffer();
        this.colorBuffer.put(colors);
        this.colorBuffer.position(0);
    }
    
    /**
     * Draw the gradient colored square.
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @see #draw(GL10 gl, Color color)
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, null);
    }
    
    /**
     * Draw the gradient colored square with the color that it is initialised with.
     * The color argument is ignores.
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color      this is ignored.
     * @see #draw(GL10)
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate, Color color) {
        //Set the color to white, e.g. full brightness
        gl.glColor4f(1f, 1f, 1f, 1f); 
        
        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW);
        
        //Point to our buffers
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.getVertexBuffer());
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, this.colorBuffer);
        
        //Enable vertex and color state
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        //Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.getVertices().length / 3);
        
        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
