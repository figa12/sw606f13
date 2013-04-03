package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;

public final class Tester extends RenderableGroup {
    
    public Tester(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    /* Thin squares to be used as the axes on screen.
     * It makes it easier to 'understand' the OpenGL matrix. */
    private Square horizontalAxis = new Square(1280f, 1.1f);
    private Square verticalAxis = new Square(1.1f, 752f);
    
    @Override
    public void load() {
        this.horizontalAxis.addCoordinate(-this.horizontalAxis.getWidth()/2f, this.horizontalAxis.getHeight()/2f, super.gameDrawer.FOREGROUND);
        this.verticalAxis.addCoordinate(-this.verticalAxis.getWidth()/2f, this.verticalAxis.getHeight()/2f, super.gameDrawer.FOREGROUND);
    }

    @Override
    public void draw() {
        super.translateAndDraw(this.horizontalAxis, Color.TransparentBlack);
        super.translateAndDraw(this.verticalAxis, Color.TransparentBlack);
    }
}
