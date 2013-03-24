package dk.aau.cs.giraf.train.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


/**
 * This class ({@code GlView}) extends {@link GLSurfaceView}.
 * Touch events for this surface is created here.
 * @author jerian
 * @see GlRenderer
 */
public class GlView extends GLSurfaceView {
    
    /**
     * Set {@link GLSurfaceView} settings.
     * @param context send to renderer
     */
    private void setup(Context context) {
        this.setEGLContextClientVersion(1); // Pick an OpenGL ES 1 context.
        
        this.setEGLConfigChooser(false);
        
        //this.setPreserveEGLContextOnPause(true); // TODO investigate
        
        this.setRenderer(new GlRenderer(context));
    }
    
    /**
     * Creates the GL surface view.
     * @param context
     */
    public GlView(Context context) {
        super(context);
        this.setup(context);
    }
    
    /**
     * Creates the GL surface view.
     * @param context
     * @param attrs
     */
    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup(context);
    }
    
    

}
