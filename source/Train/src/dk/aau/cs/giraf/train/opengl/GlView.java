package dk.aau.cs.giraf.train.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GlView extends GLSurfaceView {

    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setRenderer(new GlRenderer(context));
    }
}
