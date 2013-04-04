package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Weather extends RenderableGroup {

    public Weather(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }
    
    private Texture sun = new Texture(1f, 1f);
    
    @Override
    public void load() {
        this.sun.addCoordinate(472.8f, 310.96f, super.gameDrawer.FOREGROUND);
        
        this.sun.loadTexture(super.gl, super.context, R.drawable.texture_sun, Texture.AspectRatio.BitmapOneToOne);
    }

    @Override
    public void draw() {
        super.translateAndDraw(this.sun);
    }
}