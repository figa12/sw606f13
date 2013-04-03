package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;

public final class Weather extends RenderableGroup {

    public Weather(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private Square bigCloud = new Square(1f, 1f);
    private Square smallCloud = new Square(1f, 1f);
    private float heightLimit; // final?

    @Override
    public void load() {
        // TODO Auto-generated method stub
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
    }
}