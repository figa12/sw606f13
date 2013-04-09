package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.renderscript.Program.TextureType;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Middleground extends RenderableGroup {
    
    public Middleground(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private RenderableMatrix sequence = new RenderableMatrix();
  //  private Square square = new Square(100f, 100f);
    private Texture hills = new Texture(8096f, 752f);
    
    
    @Override
    public void load() {
    //  this.sequence.addCoordinate(500f, 300f, GameData.MIDDLEGROUND-500f);
      //  this.sequence.addCoordinate(198.92f, -87f, GameData.MIDDLEGROUND);

    	  this.sequence.addCoordinate(-1275f, 47f, GameData.MIDDLEGROUND);
    	  
        for (float i = 0f; i <= 8000f; i += this.hills.getWidth()-30f) {
            this.sequence.addRenderableMatrixItem(this.hills, new Coordinate(i, 0f, 0f));
        }
        
        this.hills.loadTexture(super.gl, super.context, R.drawable.texture_hills);
    }

    @Override
    public void draw() {
        //super.translateAndDraw(this.sequence);
        super.translateAndDraw(this.sequence);
        this.sequence.move(GameData.pixelMovementForThisFrame, 0f);
    }
}
