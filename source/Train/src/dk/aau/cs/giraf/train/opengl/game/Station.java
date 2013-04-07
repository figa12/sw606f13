package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Station extends RenderableGroup {
    
    public Station(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }
    
    private Texture trainStation = new Texture(1.0f, 1.0f);
    private Texture platform = new Texture(1280f, 100f);
    private Square trainStopper = new Square(150f, 150f);
    
    private RenderableMatrix stationPlatformMatrix = new RenderableMatrix();
    
    @Override
    public final void load() {
        //Add coordinates to the renderables
        this.trainStation.addCoordinate(-588.64f, 376f, GameData.FOREGROUND);
        this.stationPlatformMatrix.addCoordinate(-640f, -207f, GameData.FOREGROUND);
        
        //Load the textures
        this.trainStation.loadTexture(super.gl, super.context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        float xPosition = -364f; // first platform position
        
        for (int i = 0; i < GameData.numberOfStations; i++) {
            this.stationPlatformMatrix.addRenderableMatrixItem(this.platform, new Coordinate(xPosition, 0f, GameData.FOREGROUND));
            xPosition += GameData.distanceBetweenStations + this.platform.getWidth();
        }
        
        xPosition -= (GameData.distanceBetweenStations + this.platform.getWidth());
        
        this.stationPlatformMatrix.addRenderableMatrixItem(this.trainStopper, new Coordinate(xPosition + 1575f, 0f, GameData.FOREGROUND), Color.Black);
    }
    
    private float nextStopPosition = -640f;
    
    private final void checkTrainPosition() {
        // if close to station, start braking
        if(GameData.totalDistanceTraveled + GameData.brakingDistance() >= this.nextStopPosition) {
            this.nextStopPosition += GameData.distanceBetweenStations + this.platform.getWidth();
            GameData.decelerateTrain();
            GameData.numberOfStops++;
        }
    }
    
    @Override
    public final void draw() {
        //super.translateAndDraw(this.trainStation);
        super.translateAndDraw(this.stationPlatformMatrix);
        this.stationPlatformMatrix.move(GameData.pixelMovementForThisFrame, 0f);
        
        this.checkTrainPosition();
    }
}
