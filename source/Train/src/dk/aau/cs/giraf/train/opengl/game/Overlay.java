package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;

public final class Overlay extends RenderableGroup {
    
    Square overlay = new Square(1280f, 752f);
    
    public Overlay(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    @Override
    public void load() {
        //Add coordinates
        this.overlay.addCoordinate(-640f, 376f, -907f);
    }

    @Override
    public void draw() {
        if(GameData.isPaused) {
            super.translateAndDraw(this.overlay, Color.PausedOverlay);
        }
    }
}
