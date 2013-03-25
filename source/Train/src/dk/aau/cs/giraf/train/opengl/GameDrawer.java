package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;


/**
 * This class handles all drawing.
 * @author jerian
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadAllTexture()
 */
public final class GameDrawer {
    
    private interface GameDrawable {
        public void draw();
    }
    
    private interface GameDrawableTexture extends GameDrawable {
        public void loadTexture();
    }
    
    private GL10 gl;
    private Context context;
    private float visibleWidth = 1.0f;
    private float visibleHeight = 1.0f;
    private final float DRAWING_DEPTH = -907.744f;
    
    private ArrayList<GameDrawable> gameDrawables = new ArrayList<GameDrawable>();
    private ArrayList<GameDrawableTexture> gameDrawablesWithTexture = new ArrayList<GameDrawableTexture>();
    
    
    public GameDrawer(GL10 gl, Context context) {
        this.gl = gl;
        this.context = context;
        
        // add GameDrawables to the list in the order they should be drawn
        this.addGameDrawable(new Train());
    }
    
    public void setVisibleLimits(float visibleWidth, float visibleHeight) {
        this.visibleWidth = visibleWidth;
        this.visibleHeight = visibleHeight;
    }
    
    private final void addGameDrawable(GameDrawable gameDrawable) {
        this.gameDrawables.add(gameDrawable);
        
        if(gameDrawable instanceof GameDrawableTexture) {
            this.gameDrawablesWithTexture.add((GameDrawableTexture) gameDrawable);
        }
    }
    
    /** Draw everything on screen */
    public final void drawGame() {
        this.gl.glLoadIdentity(); //Reset The Current Modelview Matrix
        
        for (GameDrawable gameDrawable : this.gameDrawables) {
            gameDrawable.draw();
        }
    }
    
    /** Loads all texture */
    public final void loadAllTexture() {
        for (GameDrawableTexture gameDrawableTexture : this.gameDrawablesWithTexture) {
            gameDrawableTexture.loadTexture();
        }
        this.gameDrawablesWithTexture.clear();
    }
    
    private final class Train implements GameDrawableTexture {
        
        private Texture train = new Texture(1.0f, 1.0f);
        
        @Override
        public void draw() {
            gl.glLoadIdentity(); // reset the position
            
            gl.glTranslatef(-visibleWidth/2, visibleHeight/2, DRAWING_DEPTH);
            this.train.draw(gl);
        }

        @Override
        public void loadTexture() {
            this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
            
        }
    }
}
