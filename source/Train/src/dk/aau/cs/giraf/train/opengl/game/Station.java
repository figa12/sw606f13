package dk.aau.cs.giraf.train.opengl.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlPictogram;
import dk.aau.cs.giraf.train.opengl.Renderable;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.RuntimeLoader;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Station extends GameDrawable implements RuntimeLoader {
    
    public Station(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
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
    
    private final Texture redTrainStation = new Texture(1.0f, 1.0f);
    private final Texture yellowTrainStation = new Texture(1.0f, 1.0f);
    private final Texture blueTrainStation = new Texture(1.0f, 1.0f);
    private final Texture platform = new Texture(1280f, 100f);
    
    private final RenderableMatrix stationPlatformMatrix = new RenderableMatrix();
    private final RenderableMatrix stationPictogramMatrix = new RenderableMatrix();
    private final RenderableMatrix categoryMatrix = new RenderableMatrix();
    
    @Override
    public final void load() {
        //Add coordinates to the renderables
        //this.redTrainStation.addCoordinate(-588.64f, 376f, GameData.FOREGROUND);
        this.stationPlatformMatrix.addCoordinate(-640f, -207f, GameData.FOREGROUND);
        this.stationPictogramMatrix.addCoordinate(-600f, 9f, GameData.FOREGROUND);
        this.categoryMatrix.addCoordinate(-270f, 341f, GameData.FOREGROUND);
        
        //Load the textures
        this.redTrainStation.loadTexture(super.gl, super.context, R.drawable.texture_red_train_station, Texture.AspectRatio.BitmapOneToOne);
        this.yellowTrainStation.loadTexture(super.gl, super.context, R.drawable.texture_yellow_train_station, Texture.AspectRatio.BitmapOneToOne);
        this.blueTrainStation.loadTexture(super.gl, super.context, R.drawable.texture_blue_train_station, Texture.AspectRatio.BitmapOneToOne);
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        //Add stations to list and randomise
        ArrayList<StationContainer> stations = new ArrayList<StationContainer>();
        stations.add(new StationContainer(redTrainStation, 364f + (640f - 588.64f) - 16f, 583f));  
        stations.add(new StationContainer(blueTrainStation, 364f + (640f - 588.64f) - 16f, 583f));
        stations.add(new StationContainer(yellowTrainStation, 364f + (640f - 588.64f) - 16f, 583f));
        
        Collections.shuffle(stations);
        LinkedList<StationContainer> stationsQueue = this.getQueue(stations);
        
        //Add stations to the matrix in the randomised order
        float xPosition = -364f; // first platform position
        
        for (int i = 0; i < super.gameData.numberOfStations; i++) {
            StationContainer nextStation = stationsQueue.pop();
            stationsQueue.add(nextStation);
            this.stationPlatformMatrix.addRenderableMatrixItem(nextStation.station, new Coordinate(xPosition + nextStation.xOffset, nextStation.yOffset, 0f));
            this.stationPlatformMatrix.addRenderableMatrixItem(this.platform, new Coordinate(xPosition, 0f, 0f));
            
            //The first station does not have a category, skip
            if(i == 0) {
                xPosition += GameData.DISTANCE_BETWEEN_STATIONS;
                continue;
            }
            
            @SuppressWarnings("static-access")
            Pictogram category = PictoFactory.INSTANCE.getPictogram(super.context, super.gameData.getGameConfiguration().getStation(i - 1).getCategory());
            GlPictogram categoryTexture = new GlPictogram(100f, 100f);
            categoryTexture.loadPictogram(super.gl, super.context, category);
            
            //this.categoryMatrix.addRenderableMatrixItem(new Square(categoryWidth, categoryHeight), new Coordinate(xPosition + 364f, 0f, 0f), Color.White);
            this.categoryMatrix.addRenderableMatrixItem(categoryTexture, new Coordinate(xPosition + 364f, 0f, 0f));
            
            xPosition += GameData.DISTANCE_BETWEEN_STATIONS;
        }
        
        xPosition -= (GameData.DISTANCE_BETWEEN_STATIONS);
    }
    
    public final void calculateStoppingPositions() {
        //Unfortunately we need the size of the platform now
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        //Make new array
        super.gameData.nextStoppingPosition = new float[super.gameData.numberOfStations + 1];
        
        //Calculate all stopping positions
        super.gameData.nextStoppingPosition[0] = GameData.DISTANCE_BETWEEN_STATIONS;
        for (int i = 1; i < super.gameData.numberOfStations; i++) {
            super.gameData.nextStoppingPosition[i] += super.gameData.nextStoppingPosition[i-1] + GameData.DISTANCE_BETWEEN_STATIONS;
        }
    }
    
    private final LinkedList<StationContainer> getQueue(ArrayList<StationContainer> list) {
        LinkedList<StationContainer> queue = new LinkedList<Station.StationContainer>();
        for (StationContainer stationContainer : list) {
            queue.add(stationContainer);
        }
        return queue;
    }
    
    public final void setPictograms(int stationIndex, Pictogram[] pictograms) {
        int width = 310; //LinearLayout width
        
        //Set pictogramWidth to the size we have available relative to how many pictograms we need to fit on the provided space.
        float pictogramWidthSpace = (pictograms.length <= 4) ? width / 2 : width / 3;
        float pictogramHeightSpace = pictogramWidthSpace; //Same height as width;
        
        float xPosition = (stationIndex == 0) ? 0f : super.gameData.nextStoppingPosition[stationIndex - 1];
        final float yPosition = 0f;
        
        xPosition += 0.65f; //offset
        
        for (int i = 0; i < pictograms.length; i++, xPosition += pictogramWidthSpace) {
            if(pictograms[i] != null) {
                
                GlPictogram pictogramTexture = new GlPictogram(pictogramWidthSpace, pictogramHeightSpace);
                pictogramTexture.loadPictogram(gl, context, pictograms[i]);
                
                //this.stationPictogramMatrix.addRenderableMatrixItem(new Square(pictogramWidthSpace, pictogramHeightSpace), new Coordinate(xPosition, 0f, 0f), Color.White);
                this.stationPictogramMatrix.addRenderableMatrixItem(pictogramTexture, new Coordinate(xPosition, yPosition, 0f));
            }
            
            if(i == (pictograms.length /2) - 1) {
                xPosition += 139.8f; //Add offset to get to next LinearLayout
            }
        }
    }
    
    private int stationIndex;
    private Pictogram[] pictograms;
    private boolean readyToLoad = false;
    
    public void loadStationPictograms(int stationIndex, Pictogram[] pictograms) {
        this.stationIndex = stationIndex;
        this.pictograms = pictograms;
        this.readyToLoad = true;
    }
    
    @Override
    public void runtimeLoad() {
        this.setPictograms(this.stationIndex, this.pictograms);
        this.readyToLoad = false;
    }

    @Override
    public boolean isReadyToLoad() {
        return this.readyToLoad;
    }
    
    @Override
    public final void draw() {
        //Move
        this.stationPlatformMatrix.move(super.gameData.getPixelMovement(), 0f);
        this.stationPictogramMatrix.move(super.gameData.getPixelMovement(), 0f);
        this.categoryMatrix.move(super.gameData.getPixelMovement(), 0f);
        
        //Draw
        super.translateAndDraw(this.stationPlatformMatrix);
        super.translateAndDraw(this.stationPictogramMatrix);
        super.translateAndDraw(this.categoryMatrix);
    }
}
