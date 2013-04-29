package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlPictogram;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Train extends GameDrawable {

    public Train(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }

    private final Texture train = new Texture(1.0f, 1.0f);
    private final Texture wagon = new Texture(1.0f, 1.0f);
    private final Square shaft = new Square(40f, 3f);
    private final Square trainWindow = new Square(95f, 95f);
    
    @Override
    public final void load() {
        //Load the textures
        this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
        this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
        
        //Add coordinates to the renderables
        this.wagon.addCoordinate(-542.32f, -142.72f, GameData.FOREGROUND);
        this.wagon.addCoordinate(-187.45f, -142.72f, GameData.FOREGROUND);
        this.shaft.addCoordinate(-227.45f, -294.72f, GameData.FOREGROUND);
        this.shaft.addCoordinate(127.42f, -294.72f, GameData.FOREGROUND);
        this.train.addCoordinate(160.42f, -52.37f, GameData.FOREGROUND);
        this.trainWindow.addCoordinate(198.92f, -87f, GameData.FOREGROUND);
    }
    
    private GlPictogram driverPictogram;
    
    public final void setDriverPictogram(Pictogram pictogram) {
        if(pictogram == null) {
            this.driverPictogram = null;
            return;
        }
        
        this.driverPictogram = new GlPictogram(100f, 100f);
        this.driverPictogram.loadPictogram(gl, context, pictogram);
        this.driverPictogram.addCoordinate(197f, -84f, GameData.FOREGROUND);
    }
    
    @Override
    public final void draw() {
        super.translateAndDraw(this.shaft, Color.Black);
        super.translateAndDraw(this.wagon);
        super.translateAndDraw(this.trainWindow, Color.Window);
        super.translateAndDraw(this.train);
        
        if(this.driverPictogram != null) {
            super.translateAndDraw(this.driverPictogram);
        }
    }
}
