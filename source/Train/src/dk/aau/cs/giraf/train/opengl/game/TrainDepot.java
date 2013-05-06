package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
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
	
	public TrainDepot(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData, int depth) {
        super(gl, context, gameDrawer, gameData);
        
        this.depth = depth;
    }
    
	private final Texture trainDepot = new Texture(1.0f , 1.0f);
	private final Square trainDepotBackside = new Square(1178f, 440f);
	private final RenderableMatrix trainDepotMatrix = new RenderableMatrix();

	@Override
	public final void load() {
		
		this.trainDepotMatrix.addCoordinate(-640f, 305.38f, GameData.FOREGROUND);
		
		super.gameData.nextStoppingPosition[super.gameData.numberOfStations - 1] = super.gameData.nextStoppingPosition[super.gameData.numberOfStations - 2] + GameData.DISTANCE_TO_DEPOT;	
		super.gameData.nextStoppingPosition[super.gameData.numberOfStations] = Float.MAX_VALUE; //Have to give the last index a value equal or greater than the last index.
		
		float offset = 45.79f;
		
		switch(this.depth) {
		case TrainDepot.BEFORE_TRAIN:
			this.trainDepotMatrix.addRenderableMatrixItem(this.trainDepotBackside, new Coordinate(super.gameData.nextStoppingPosition[super.gameData.numberOfStations - 1] + offset, -220.168f, 0f), Color.DepotBackside);
			break;
		
		case TrainDepot.AFTER_TRAIN:
		    this.trainDepot.loadTexture(super.gl, super.context, R.drawable.texture_train_depot, Texture.AspectRatio.BitmapOneToOne);
			this.trainDepotMatrix.addRenderableMatrixItem(this.trainDepot, new Coordinate(super.gameData.nextStoppingPosition[super.gameData.numberOfStations - 1] + offset, 0f, 0f));
			break;
		}
	}

	@Override
	public final void draw() {
		//Move
		this.trainDepotMatrix.move(super.gameData.getPixelMovement(), 0f);
		
		//Draw
		super.translateAndDraw(this.trainDepotMatrix);	
	} 
}
