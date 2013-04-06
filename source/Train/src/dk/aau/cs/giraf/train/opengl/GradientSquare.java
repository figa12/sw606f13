package dk.aau.cs.giraf.train.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A {@link Renderable} gradient square that extends {@link Square}.
 * Two colors can be specified for 
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
    
    public GradientSquare(float width, float height, Color firstColor, Color secondColor, GradientStyle style) {
        super(width, height);
        this.initialiseColorBuffer(firstColor, secondColor, style);
    }
    
    private void initialiseColorBuffer(Color firstColor, Color secondColor, GradientStyle style) {
        float colors[];
        
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
            return; // abort
        }
        
        // Initiate the color buffer
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
