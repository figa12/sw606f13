package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.GradientSquare;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Weather extends GameDrawable {
        
    public Weather(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }
    
    private final Texture sun = new Texture(450f, 450f);
    private GradientSquare backgroundGradient;
    
    @Override
    public final void load() {
        //Create background gradient
        float height = GlRenderer.getActualHeight(GameData.BACKGROUND);
        float width = GlRenderer.getActualWidth(height);
        this.backgroundGradient = new GradientSquare(width, height, Color.BackgroundTopColor, Color.BackgroundBottomColor, GradientSquare.GradientStyle.Vertical);
        
        //Load the textures
        this.sun.loadTexture(super.gl, super.context, R.drawable.texture_sun);
        
        //Add coordinates to the renderables
        float cornerOffset = 100f;
        this.sun.addCoordinate(width/2 - this.sun.getWidth() - cornerOffset, height/2 - cornerOffset, GameData.BACKGROUND);
        this.backgroundGradient.addCoordinate(-width/2, height/2, GameData.BACKGROUND);
    }

    @Override
    public final void draw() {
        super.translateAndDraw(this.backgroundGradient);
        super.translateAndDraw(this.sun);
    }
}
