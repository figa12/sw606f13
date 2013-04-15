package dk.aau.cs.giraf.train.opengl.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Renderable;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Station extends RenderableGroup {
    
    public Station(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }
    
    private class StationContainer {
        public Renderable station;
        public float xOffset;
        public float yOffset;
        
        public StationContainer(Renderable station, float xOffset, float yOffset) {
            this.station = station;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }
    
    private Texture redTrainStation = new Texture(1.0f, 1.0f);
    private Texture platform = new Texture(1280f, 100f);
    private Square trainStopper = new Square(150f, 150f);
    
    private RenderableMatrix stationPlatformMatrix = new RenderableMatrix();
    
    @Override
    public final void load() {
        //Add coordinates to the renderables
        this.redTrainStation.addCoordinate(-588.64f, 376f, GameData.FOREGROUND);
        this.stationPlatformMatrix.addCoordinate(-640f, -207f, GameData.FOREGROUND);
        
        //Load the textures
        this.redTrainStation.loadTexture(super.gl, super.context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        //Add stations to list and randomise
        ArrayList<StationContainer> stations = new ArrayList<StationContainer>();
        stations.add(new StationContainer(redTrainStation, 364f + (640f - 588.64f) - 16f, 583f));
        
        Collections.shuffle(stations);
        LinkedList<StationContainer> stationsQueue = this.getQueue(stations);
        
        //Add stations to the matrix
        float xPosition = -364f; // first platform position
        
        for (int i = 0; i < GameData.numberOfStations; i++) {
            StationContainer nextStation = stationsQueue.pop();
            stationsQueue.push(nextStation);
            this.stationPlatformMatrix.addRenderableMatrixItem(nextStation.station, new Coordinate(xPosition + nextStation.xOffset, nextStation.yOffset, 0f));
            this.stationPlatformMatrix.addRenderableMatrixItem(this.platform, new Coordinate(xPosition, 0f, 0f));
            
            xPosition += GameData.distanceBetweenStations + this.platform.getWidth();
        }
        
        xPosition -= (GameData.distanceBetweenStations + this.platform.getWidth());
        
        this.stationPlatformMatrix.addRenderableMatrixItem(this.trainStopper, new Coordinate(xPosition + 1575f, 0f, 0f), Color.Black);
    }
    
    public final void calculateStoppingPositions() {
        //Unfortunately we need the size of the platform now
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        //Make new array
        GameData.nextStoppingPosition = new float[GameData.numberOfStations];
        
        //Calculate all stopping positions
        GameData.nextStoppingPosition[0] = GameData.distanceBetweenStations + this.platform.getWidth();
        for (int i = 1; i < GameData.numberOfStations; i++) {
            GameData.nextStoppingPosition[i] += GameData.nextStoppingPosition[i-1] + GameData.distanceBetweenStations + this.platform.getWidth();
        }
    }
    
    private final LinkedList<StationContainer> getQueue(ArrayList<StationContainer> list) {
        LinkedList<StationContainer> queue = new LinkedList<Station.StationContainer>();
        for (StationContainer stationContainer : list) {
            queue.push(stationContainer);
        }
        return queue;
    }
    
    @Override
    public final void draw() {
        //Move
        this.stationPlatformMatrix.move(GameData.pixelMovementForThisFrame, 0f);
        
        //Draw
        super.translateAndDraw(this.stationPlatformMatrix);
    }
}
