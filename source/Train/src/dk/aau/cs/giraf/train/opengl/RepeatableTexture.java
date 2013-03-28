package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import dk.aau.cs.giraf.train.opengl.Texture.AspectRatio;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * The class extends {@link Texture}. The given texture is repeated relative to the repeat-style.
 * This class has compatibility issues.
 * @author Jesper
 * @see Texture
 */
public class RepeatableTexture extends Texture {
    
    protected Style style;
    protected float numberOfRepeats;
    
    public RepeatableTexture(float width, float height, Style style, float numberOfRepeats) {
        super(width, height);
        super.GENERATE_POWER_OF_TWO_EQUIVALENT = false; // will not work with repeatable texture
        
        this.style = style;
        this.numberOfRepeats = numberOfRepeats;
    }
    
    @Override
    public void loadTexture(GL10 gl, Context context, int resourcePointer) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourcePointer);
        
        this.setRepeatableTexture(style, bitmap.getWidth(), bitmap.getHeight(), this.numberOfRepeats);
        
        super.generateTexturePointer(gl, context, bitmap, AspectRatio.KeepBoth, GL10.GL_REPEAT);
    }
    
    @Override
    public void loadTexture(GL10 gl, Context context, int resourcePointer, AspectRatio option) {
        this.loadTexture(gl, context, resourcePointer); // Disallow changing aspect ratio
    }
    
    public enum Style {
        RepeatHorizontally, RepeatVertically
    }
    
    protected void setRepeatableTexture(Style option, int bitmapWidth, int bitmapHeight, float numberOfRepeats) {
        // investigate when method should be called
        switch(option) {
        case RepeatHorizontally: // numberOfRepeats = vertically
            super.setTextureCoordinates(new float[] {
                    // Mapping coordinates for the vertices
                    0.0f, 0.0f,                                                         // bottom left  (V1)
                    numberOfRepeats * (super.getWidth() / bitmapWidth), 0.0f,           // bottom right (V3)
                    0.0f, numberOfRepeats,                                              // top left     (V2)
                    numberOfRepeats * (super.getWidth() / bitmapWidth), numberOfRepeats // top right    (V4)
            });
            break;
        case RepeatVertically: // numberOfRepeats = horizontally
            super.setTextureCoordinates(new float[] {
                    // Mapping coordinates for the vertices
                    0.0f, 0.0f,                                                           // bottom left  (V1)
                    numberOfRepeats, 0.0f,                                                // bottom right (V3)
                    0.0f, numberOfRepeats * (super.getHeight() / bitmapHeight),           // top left     (V2)
                    numberOfRepeats, numberOfRepeats * (super.getHeight() / bitmapHeight) // top right    (V4)
            });
            break;
        }
    }
    
    protected void setRepeatableTexture(Style option, int bitmapWidth, int bitmapHeight) {
        this.setRepeatableTexture(option, bitmapWidth, bitmapHeight, 1.0f);
    }
}
