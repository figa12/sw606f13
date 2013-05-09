package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.Square;

public final class Overlay extends GameDrawable {
    
    public Overlay(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }
    
    Square overlay;
    
    @Override
    public void load() {
        //Create objects
        float height = GlRenderer.getActualHeight(GameData.FOREGROUND);
        float width = GlRenderer.getActualWidth(height);
        this.overlay = new Square(width, height);
        
        //Add coordinates
        this.overlay.addCoordinate(-width/2, height/2, GameData.FOREGROUND);
    }

    @Override
    public void draw() {
        if(super.gameData.isPaused) {
            super.translateAndDraw(this.overlay, Color.PausedOverlay);
        }
    }
}
