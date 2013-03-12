package dk.aau.cs.giraf.train.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGlView extends GLSurfaceView {

    public MyGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setRenderer(new Lesson03());
    }
    
}
