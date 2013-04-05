package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Train extends RenderableGroup {

    public Train(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private Texture train = new Texture(1.0f, 1.0f);
    private Texture wagon = new Texture(1.0f, 1.0f);
    private Square shaft = new Square(40f, 3f);
    private Square trainWindow = new Square(95f, 95f);
    
    @Override
    public final void load() {
        //Add coordinates to the renderables
        this.wagon.addCoordinate(-542.32f, -142.72f, super.gameDrawer.FOREGROUND);
        this.wagon.addCoordinate(-187.45f, -142.72f, super.gameDrawer.FOREGROUND);
        this.shaft.addCoordinate(-227.45f, -294.72f, super.gameDrawer.FOREGROUND);
        this.shaft.addCoordinate(127.42f, -294.72f, super.gameDrawer.FOREGROUND);
        this.train.addCoordinate(160.42f, -52.37f, super.gameDrawer.FOREGROUND);
        this.trainWindow.addCoordinate(198.92f, -87f, super.gameDrawer.FOREGROUND);
        
        //Load the textures
        this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
        this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
    }
    
    @Override
    public final void draw() {
        super.translateAndDraw(this.shaft, Color.Black);
        super.translateAndDraw(this.wagon);
        super.translateAndDraw(this.trainWindow, Color.Window);
        super.translateAndDraw(this.train);
        
        this.toggleVelocity();
    }
    
    private int temp = 1;
    private float timeSinceLast = 0f;
    
    
    private void toggleVelocity() {
        this.timeSinceLast += GameData.timeDifference;
        
        if(this.timeSinceLast > 16000f) {
            this.timeSinceLast = 0f;
            
            if(temp++ % 2 == 0) {
                GameData.decelerateTrain();
            }
            else {
                GameData.accelerateTrain();
            }
        }
    }
}