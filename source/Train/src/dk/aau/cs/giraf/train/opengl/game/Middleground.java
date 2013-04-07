package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;

public final class Middleground extends RenderableGroup {
    
    public Middleground(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private RenderableMatrix sequence = new RenderableMatrix();
    private Square square = new Square(100f, 100f);
    
    @Override
    public void load() {
        this.sequence.addCoordinate(500f, 300f, GameData.MIDDLEGROUND-500f);
        this.sequence.addCoordinate(198.92f, -87f, GameData.MIDDLEGROUND);
        
        for (float i = 0f; i <= 8000f; i += 100f) {
            this.sequence.addRenderableMatrixItem(square, new Coordinate(i, 0f, 0f), Color.Blue);
            i += 100f;
            this.sequence.addRenderableMatrixItem(square, new Coordinate(i, 0f, 0f), Color.Green);
        }
    }

    @Override
    public void draw() {
        super.translateAndDraw(this.sequence);
        
        this.sequence.move(GameData.pixelMovementForThisFrame, 0f);
    }
}
