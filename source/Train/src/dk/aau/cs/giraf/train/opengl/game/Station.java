package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Station extends RenderableGroup {
    
    public Station(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private Texture trainStation = new Texture(1.0f, 1.0f);
    
    @Override
    public final void load() {
        //Add coordinates to the renderables
        trainStation.addCoordinate(-588.64f, 376f, super.gameDrawer.FOREGROUND);
        
        //Load the textures
        this.trainStation.loadTexture(super.gl, super.context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
    }
    
    @Override
    public final void draw() {
        super.translateAndDraw(this.trainStation);
    }
}
