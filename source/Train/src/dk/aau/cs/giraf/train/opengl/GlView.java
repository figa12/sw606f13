package dk.aau.cs.giraf.train.opengl;

import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.game.GameData;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * This class ({@code GlView}) extends {@link GLSurfaceView}.
 * Touch events for this surface is created here.
 * @author Jesper
 * @see GlRenderer
 */
public class GlView extends GLSurfaceView {
    
    private final SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private final int sound = soundPool.load(this.getContext(), R.raw.koere, 1);
    
    /**
     * Set {@link GLSurfaceView} settings.
     * @param context send to renderer
     */
    private void setup(Context context) {
        this.setEGLContextClientVersion(1); // Pick an OpenGL ES 1 context. Going old school because its easier
        
        this.setEGLConfigChooser(true);
        
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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //long eventDuration = event.getEventTime() - event.getDownTime(); // the time the screen is touched
        
        //float x = event.getX();
        //float y = event.getY();
        //Log.d(GlView.class.getSimpleName(), "Touched: " + Float.toString(x) + " x " + Float.toString(y));
        
        if(event.getAction() == MotionEvent.ACTION_DOWN && GameData.isPaused) {
            GameData.onResume();
        }
        else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(GameData.currentTrainVelocity == 0f && GameData.numberOfStops < GameData.numberOfStations - 1) {
                GameData.accelerateTrain();
                this.soundPool.play(sound, 1f, 1f, 0, 0, 0.75f);
            }
        }
        return true;
    }

}
