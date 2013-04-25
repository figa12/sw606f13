package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;
import android.content.Context;
import dk.aau.cs.giraf.train.R;


public final class TrainDepot extends GameDrawable {
	
	// Using integers instead of Enums for less overhead
	public static final int BEFORE_TRAIN = 0;
	public static final int AFTER_TRAIN = 1;
	private int depth;
	
	public TrainDepot(GL10 gl, Context context, GameDrawer gameDrawer, int depth) {
        super(gl, context, gameDrawer);
        
        this.depth = depth;
    }
    
	private final Texture trainDepot = new Texture(1.0f , 1.0f);
	private final Texture trainDepotBackside = new Texture(1.0f, 1.0f);
	private final RenderableMatrix trainDepotMatrix = new RenderableMatrix();

	@Override
	public final void load() {
		
		this.trainDepotMatrix.addCoordinate(-640f, 305.38f, GameData.FOREGROUND);
		
		this.trainDepot.loadTexture(super.gl, super.context, R.drawable.texture_train_depot, Texture.AspectRatio.BitmapOneToOne);
		this.trainDepotBackside.loadTexture(super.gl, super.context, R.drawable.texture_train_depot_background, Texture.AspectRatio.BitmapOneToOne);
		
		GameData.nextStoppingPosition[GameData.numberOfStations - 1] = GameData.nextStoppingPosition[GameData.numberOfStations - 2] + GameData.distanceToDepot;	
		GameData.nextStoppingPosition[GameData.numberOfStations] = GameData.nextStoppingPosition[GameData.numberOfStations - 1] + 1337f; // Have to give the last index a value equal or greater than the last index.
		
		switch(this.depth) {
		
		case TrainDepot.BEFORE_TRAIN:
			
			this.trainDepotMatrix.addRenderableMatrixItem(this.trainDepotBackside, new Coordinate(GameData.nextStoppingPosition[GameData.numberOfStations], -220.168f, 0f));
			int f = 0;
			
			break;
		
		case TrainDepot.AFTER_TRAIN:
			
			this.trainDepotMatrix.addRenderableMatrixItem(this.trainDepot, new Coordinate(GameData.nextStoppingPosition[GameData.numberOfStations], 0f, 0f));
			int t = 0;
			
			break;
	
		}
		
	}

	@Override
	public final void draw() {
		//Move
		this.trainDepotMatrix.move(GameData.pixelMovementForThisFrame, 0f);
		
		//Draw
		super.translateAndDraw(this.trainDepotMatrix);		
		
	} 
}