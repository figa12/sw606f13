package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


/**
 * Should have a power-of-two size //TODO more explaining
 * @author jerian
 * @see Texture
 */
public class RepeatableTexture extends Texture {
    
    private float textureWidth;
    private float textureheight;
    
    public RepeatableTexture(float width, float height) {
        super(width, height);
    }
    
    @Override
    public void loadTexture(GL10 gl, Context context, int resourcePointer) {
        super.generateTexturePointer(gl, context, resourcePointer, Texture.AspectRatio.KeepBoth, GL10.GL_REPEAT);
    }
    
    @Override
    public void loadTexture(GL10 gl, Context context, int resourcePointer, AspectRatio option) {
        this.loadTexture(gl, context, resourcePointer); // Disallow changing aspect ratio
    }
    
    public enum Style {
        RepeatHorizontally, RepeatVertically, RepeatBoth
    }
    
    public void setRepeatableTexture(Style option, float numberOfRepeats) {
        // investigate whether this should be called before or after loading texture
        switch(option) {
        case RepeatBoth:
            // use bitmap size
            break;
        case RepeatHorizontally:
            
            break;
        case RepeatVertically:
            
            break;
        default:
            break;
        }
    }
    
    public void setRepeatableTexture(Style option) {
        this.setRepeatableTexture(option, 1);
    }
}
