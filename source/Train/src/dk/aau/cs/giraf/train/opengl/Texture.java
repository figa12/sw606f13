package dk.aau.cs.giraf.train.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * A Texture class that extends the {@link Square} class.
 * The texture will always stretch to the specified size.
 * Use {@link #loadGLTexture(GL10, Context, int)} to load the texture into OpenGL.
 * 
 * @author Jesper
 * @see Square
 * @see Shape
 */
public class Texture extends Square {
    
    /** 
     * The texture pointer to memory.
     * It must be an array since the parameter of {@link GL10#glGenTextures} takes an array
     */
    private int[] textures = new int[1];
    
    /** The buffer holding the texture coordinates */
    private FloatBuffer textureBuffer;
    
    /** The texture coordinates.
     * The order of the coordinates determines the orientation of the texture. */   
    private float textureCoordinates[] = {
        // Mapping coordinates for the vertices
        0.0f, 0.0f,    // bottom left  (V1)
        1.0f, 0.0f,    // bottom right (V3)
        0.0f, 1.0f,    // top left     (V2)
        1.0f, 1.0f     // top right    (V4)
    };
    
    /** 
     * Sets the size of the texture and initialises a {@link FloatBuffer} for the vertices.
     * 
     * @see Square#Square(float, float)
     * @param width
     * @param height
     */
    public Texture(float width, float height) {
        super(width, height);
        
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(textureCoordinates);
        textureBuffer.position(0);
    }
    
    /**
     * Draws the texture with a standard drawing method.
     * This method calls {@link #draw(GL10, float, float, float, float)} and sets the color to white with no transparency.
     */
    @Override
    public void draw(GL10 gl) {
        this.draw(gl, 1.0f, 1.0f, 1.0f, 1.0f); // White, no transparency
    }
    
    /** 
     * Draw the texture with the specified RGBA color
     * 
     * @param gl    the GL10 instance.
     * @param red   a value between 0.0f and 1.0f.
     * @param green a value between 0.0f and 1.0f.
     * @param blue  a value between 0.0f and 1.0f.
     * @param alpha a value between 0.0f and 1.0f.
     * @see #draw(GL10)
     */
    @Override
    public void draw(GL10 gl, float red, float green, float blue, float alpha) {
        gl.glColor4f(red, green, blue, alpha);
        
        //Enable texture
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        //Bind our only previously generated texture in this case
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        
        //Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW); // TODO further investigation nedded
        
        //Enable the vertex and texture state
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, super.getVertexBuffer());
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        
        //Draw the vertices as triangles, based on the Index Buffer information
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, super.getVertices().length / 3);
        
        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        //Remember to disable texture. Shapes seem to fail if texture is left enabled
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
    
    /** 
     * Loads the specified texture.
     * 
     * @param gl              the GL10 instance
     * @param context         the current activity context
     * @param resourcePointer a pointer to the resource to load
     */
    public void loadGLTexture(GL10 gl, Context context, int resourcePointer) {
        //Get the texture from the Android resource directory
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourcePointer);
        
        int newWidth = this.getNextPowerOfTwo(bitmap.getWidth());
        int newHeight = this.getNextPowerOfTwo(bitmap.getHeight());
        
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight);
        
        //Generate one texture pointer...
        gl.glGenTextures(1, textures, 0);
        //...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        
        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
        //Clean up. Sends the bitmap to the garbage collector
        bitmap.recycle();
    }
    
    private int getNextPowerOfTwo(int x) {
        return (int) Math.pow(2, Math.ceil((double) Math.log(x) / Math.log(2.0)));
    }
}
