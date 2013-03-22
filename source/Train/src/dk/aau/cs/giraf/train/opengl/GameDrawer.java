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
    
    private interface MoveableGameDrawable {
        public void Move();
        // more?
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
        this.gameDrawablesWithTexture = null;
    }
    
    private final class Train implements GameDrawableTexture {
        
        private Texture texture = new Texture(10.0f, 10.0f);
        
        @Override
        public void draw() {
            GameDrawer.this.gl.glLoadIdentity();
            GameDrawer.this.gl.glTranslatef(-4.0f, 6.0f, -20.0f);
            
            this.texture.draw(GameDrawer.this.gl);
        }

        @Override
        public void loadTexture() {
            this.texture.loadGLTexture(GameDrawer.this.gl, GameDrawer.this.context, R.drawable.train);
        }
    }
}
