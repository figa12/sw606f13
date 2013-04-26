package dk.aau.cs.giraf.train.opengl;

import dk.aau.cs.giraf.train.opengl.game.GameData;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * This class ({@code GlView}) extends {@link GLSurfaceView}.
 * Touch events for this surface is created here.
 * @author Jesper Riemer Andersen
 * @see GlRenderer
 */
public class GlView extends GLSurfaceView {
    
    private GlRenderer glRenderer;
    private GameData gameData;
    
    /**
     * Set {@link GLSurfaceView} settings.
     * @param context send to renderer
     */
    private void setup() {
        this.setEGLContextClientVersion(1); // Pick an OpenGL ES 1 context. Going old school because its easier
        
        this.setEGLConfigChooser(true);
        
        this.setPreserveEGLContextOnPause(false); //When false: onSurfaceCreated is called when app is restored
    }
    
    /**
     * Creates the GL surface view.
     * @param context
     */
    public GlView(Context context) {
        super(context);
        this.setup();
    }
    
    /**
     * Creates the GL surface view.
     * @param context
     * @param attrs
     */
    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }
    
    public void bindGameData(GameData gameData) {
        this.gameData = gameData;
        this.glRenderer = new GlRenderer(super.getContext(), this.gameData);
        super.setRenderer(this.glRenderer); //First start rendering when we have a GameData object
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        this.glRenderer.onSaveInstanceState();
        return super.onSaveInstanceState();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //long eventDuration = event.getEventTime() - event.getDownTime(); // the time the screen is touched
        
        //float x = event.getX();
        //float y = event.getY();
        
        return super.onTouchEvent(event);
    }

}
