package dk.aau.cs.giraf.train.opengl.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Renderable;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;
import dk.aau.cs.giraf.train.profile.GameConfiguration;

public final class Station extends GameDrawable {
    
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
        
        for (int i = 0; i < GameData.numberOfStations; i++) {
            StationContainer nextStation = stationsQueue.pop();
            stationsQueue.add(nextStation);
            this.stationPlatformMatrix.addRenderableMatrixItem(nextStation.station, new Coordinate(xPosition + nextStation.xOffset, nextStation.yOffset, 0f));
            this.stationPlatformMatrix.addRenderableMatrixItem(this.platform, new Coordinate(xPosition, 0f, 0f));
            
            //The first station does not have a category, skip
            if(i == 0) {
                xPosition += GameData.DISTANCE_BETWEEN_STATIONS;
                continue;
            }
            
            float categoryWidth = 100f;
            float categoryHeight = 100f;
            
            Pictogram category = PictoFactory.INSTANCE.getPictogram(super.context, super.gameData.getGameConfiguration().getStation(i - 1).getCategory());
            Bitmap categoryBitmap = BitmapFactory.decodeFile(category.getImagePath());
            Texture categoryTexture = new Texture(categoryWidth, categoryHeight);
            //Load texture relative to pictogram size. We need to maintain the aspect ratio relative to the greatest side.
            if(categoryBitmap.getWidth() >= categoryBitmap.getHeight()) {
                categoryTexture.loadTexture(super.gl, super.context, categoryBitmap, Texture.AspectRatio.KeepWidth);
            } else {
                categoryTexture.loadTexture(super.gl, super.context, categoryBitmap, Texture.AspectRatio.KeepHeight);
            }
            
            //Center image in the available space
            float xOffset = (categoryWidth - categoryTexture.getWidth()) / 2;
            float yOffset = (categoryHeight - categoryTexture.getHeight()) / 2;
            
            //this.categoryMatrix.addRenderableMatrixItem(new Square(categoryWidth, categoryHeight), new Coordinate(xPosition + 364f, 0f, 0f), Color.White);
            this.categoryMatrix.addRenderableMatrixItem(categoryTexture, new Coordinate(xPosition + 364f + xOffset, 0f - yOffset, 0f));
            
            xPosition += GameData.DISTANCE_BETWEEN_STATIONS;
        }
        
        xPosition -= (GameData.DISTANCE_BETWEEN_STATIONS);
    }
    
    public final void calculateStoppingPositions() {
        //Unfortunately we need the size of the platform now
        this.platform.loadTexture(super.gl, super.context, R.drawable.texture_platform, Texture.AspectRatio.BitmapOneToOne);
        
        //Make new array
        super.gameData.nextStoppingPosition = new float[GameData.numberOfStations + 1];
        
        //Calculate all stopping positions
        super.gameData.nextStoppingPosition[0] = GameData.DISTANCE_BETWEEN_STATIONS;
        for (int i = 1; i < GameData.numberOfStations; i++) {
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
        float pictogramWidthSpace;
        float pictogramHeightSpace;
        
        //Set pictogramWidth to the size we have available relative to how many pictograms we need to fit on the provided space.
        pictogramWidthSpace = (pictograms.length <= 4) ? width / 2 : width / 3;
        pictogramHeightSpace = pictogramWidthSpace; //Same height as width
        
        float xPosition = (stationIndex == 0) ? 0f : super.gameData.nextStoppingPosition[stationIndex - 1];
        
        for (int i = 0; i < pictograms.length; i++, xPosition += pictogramWidthSpace) {
            if(pictograms[i] != null) {
                //Decode bitmap from the image path.
                Bitmap pictogramBitmap = BitmapFactory.decodeFile(pictograms[i].getImagePath());
                
                Texture pictogramTexture = new Texture(pictogramWidthSpace, pictogramHeightSpace);
                
                //Load texture relative to pictogram size. We need to maintain the aspect ratio relative to the greatest side.
                if(pictogramBitmap.getWidth() >= pictogramBitmap.getHeight()) {
                    pictogramTexture.loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepWidth);
                } else {
                    pictogramTexture.loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepHeight);
                }
                
                //Center image in the available space
                float xOffset = (pictogramWidthSpace - pictogramTexture.getWidth()) / 2;
                float yOffset = (pictogramHeightSpace - pictogramTexture.getHeight()) / 2;
                
                this.stationPictogramMatrix.addRenderableMatrixItem(new Square(pictogramWidthSpace, pictogramHeightSpace), new Coordinate(xPosition, 0f, 0f), Color.White);
                //this.stationPictogramMatrix.addRenderableMatrixItem(pictogramTexture, new Coordinate(xPosition + xOffset, 0f - yOffset, 0f));
                
                pictogramBitmap.recycle();
            }
            
            if(i == (pictograms.length /2) - 1) {
                xPosition += 138f; //Add offset to get to next LinearLayout
            }
        }
    }
    
    @Override
    public final void draw() {
        //Move
        this.stationPlatformMatrix.move(super.gameData.pixelMovementForThisFrame, 0f);
        this.stationPictogramMatrix.move(super.gameData.pixelMovementForThisFrame, 0f);
        this.categoryMatrix.move(super.gameData.pixelMovementForThisFrame, 0f);
        
        //Draw
        super.translateAndDraw(this.stationPlatformMatrix);
        super.translateAndDraw(this.stationPictogramMatrix);
        super.translateAndDraw(this.categoryMatrix);
    }
}
