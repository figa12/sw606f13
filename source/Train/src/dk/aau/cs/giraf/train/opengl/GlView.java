package dk.aau.cs.giraf.train.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GlView extends GLSurfaceView {

    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        //setEGLContextClientVersion(2); // Pick an OpenGL ES 2.0 context.
        
        this.setEGLConfigChooser(false);
        
        //this.setPreserveEGLContextOnPause(true); // TODO investigate
        
        this.setRenderer(new GlRenderer(context));
    }
}
