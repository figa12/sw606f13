package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;


public final class GameDrawer {
    
    private interface GameDrawable {
        public void draw();
    }
    
    private interface GameDrawableTexture extends GameDrawable {
        public void loadTexture();
    }
    
    private GL10 gl;
    private Context context;
    
    private ArrayList<GameDrawable> gameDrawables = new ArrayList<GameDrawable>();
    private ArrayList<GameDrawableTexture> gameDrawablesWithTexture = new ArrayList<GameDrawableTexture>();
    
    
    public GameDrawer(GL10 gl, Context context) {
        this.gl = gl;
        this.context = context;
        
        // add GameDrawables to the list in the order they should be drawn
        this.addGameDrawable(new Train());
    }
    
    private final void addGameDrawable(GameDrawable gameDrawable) {
        this.gameDrawables.add(gameDrawable);
        
        if(gameDrawable instanceof GameDrawableTexture) {
            this.gameDrawablesWithTexture.add((GameDrawableTexture) gameDrawable);
        }
    }
    
    public final void drawGame() {
        this.gl.glLoadIdentity(); //Reset The Current Modelview Matrix
        
        for (GameDrawable gameDrawable : this.gameDrawables) {
            gameDrawable.draw();
        }
    }
    
    public final void loadAllTexture() {
        for (GameDrawableTexture gameDrawableTexture : this.gameDrawablesWithTexture) {
            gameDrawableTexture.loadTexture();
        }
        this.gameDrawablesWithTexture.clear();
    }
    
    private final class Train implements GameDrawableTexture {
        
        private Texture trainTexture = new Texture(6.0f, 6.0f);
        private Texture rails = new Texture(1.0f, 1.0f);
        
        @Override
        public void draw() {
            GameDrawer.this.gl.glLoadIdentity(); // reset the position
            
            GameDrawer.this.gl.glTranslatef(-3.0f, 0.0f, -20.0f);
            //this.rails.draw(GameDrawer.this.gl);
            
            GameDrawer.this.gl.glTranslatef(6.0f, 2.0f, 0.0f);
            this.trainTexture.draw(GameDrawer.this.gl);
        }

        @Override
        public void loadTexture() {
            this.trainTexture.loadGLTexture(GameDrawer.this.gl, GameDrawer.this.context, R.drawable.trainudenpoweroftwo, Texture.AspectRatio.KeepHeight);
            
        }
    }
}
