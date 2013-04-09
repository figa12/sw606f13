package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.GradientSquare;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Weather extends RenderableGroup {

    public Weather(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }
    
    private Texture sun = new Texture(250f, 250f);
    private GradientSquare backgroundGradient;
    
    @Override
    public void load() {
        //Create background gradient
        float height = GlRenderer.getActualHeight(GameData.BACKGROUND);
        float width = GlRenderer.getActualWidth(height);
        this.backgroundGradient = new GradientSquare(width, height, Color.BackgroundTopColor, Color.BackgroundBottomColor, GradientSquare.GradientStyle.Vertical);
        
        //Add coordinates to the renderables
        this.sun.addCoordinate(1462.61f, 985.53f, GameData.BACKGROUND);
        this.backgroundGradient.addCoordinate(-width/2, height/2, GameData.BACKGROUND);
        
        //Load the textures
        this.sun.loadTexture(super.gl, super.context, R.drawable.texture_sun);
    }

    @Override
    public void draw() {
        super.translateAndDraw(this.backgroundGradient);
        super.translateAndDraw(this.sun);
    }
}
