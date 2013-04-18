package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Texture;
import dk.aau.cs.giraf.train.opengl.game.RenderableGroup;

public final class TrainSmoke extends RenderableGroup {
    
    public TrainSmoke(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private final int numberOfSmokeClouds = 5;
    
    private final Texture smokeCloud = new Texture(1f, 1f);
    private Coordinate[] coordinates = new Coordinate[this.numberOfSmokeClouds];
    private Color[] colors = new Color[this.numberOfSmokeClouds];
    
    private final Coordinate startCoordinate = new Coordinate(455.42f, -52.37f, GameData.FOREGROUND);
    
    private int resetIndex = 0;
    private final float timeBetweenSmokeClouds = 150f; // ms
    private float timeSinceLastReset = 0f;
    private final float ySpeed = 0.15f;
    
    private final void resetOneSmokeCloud() {
        //Increment the reset index
        this.resetIndex = ++this.resetIndex % this.numberOfSmokeClouds;
        
        //Put cloud back to exhaust
        this.coordinates[this.resetIndex].setCoordinate(this.startCoordinate.getX(), this.startCoordinate.getY());
        this.colors[this.resetIndex].setColor(1f, 1f, 1f, 1f);
    }
    
    private final void updateSmokeClouds() {
        //Updates position and alpha channels
        for (int i = 0; i < this.numberOfSmokeClouds; i++) {
            //Always move smoke vertically, move smoke horizontally relative to the train speed.
            this.coordinates[i].moveX(GameData.pixelMovementForThisFrame);
            this.coordinates[i].moveY(this.ySpeed * GameData.timeDifference);
            
            //Fade the smoke
            this.colors[i].alpha -= (1f / (this.timeBetweenSmokeClouds * this.numberOfSmokeClouds)) * GameData.timeDifference;
        }
    }
    
    @Override
    public final void load() {
        //Load the texture
        this.smokeCloud.loadTexture(gl, context, R.drawable.texture_train_smoke, Texture.AspectRatio.BitmapOneToOne);
        
        //Start conditions:
        for (int i = 0; i < this.numberOfSmokeClouds; i++) {
            this.coordinates[i] = new Coordinate(this.startCoordinate.getX(), this.startCoordinate.getY(), this.startCoordinate.getZ());
            this.colors[i] = new Color(1f, 1f, 1f, 0f); // invisible
        }
    }

    @Override
    public final void draw() {
        //Reset one smoke cloud at the given interval
        this.timeSinceLastReset += GameData.timeDifference;
        if(this.timeSinceLastReset >= this.timeBetweenSmokeClouds) {
            this.timeSinceLastReset = 0f;
            
            if(!GameData.isPaused) {
                this.resetOneSmokeCloud();
            }
        }
        
        //Draw all smoke clouds.
        for (int i = 0; i < this.numberOfSmokeClouds; i++) {
            super.translateAndDraw(this.smokeCloud, this.coordinates[i], this.colors[i]);
        }
        
        if(!GameData.isPaused) {
            //Update position and alpha channels
            this.updateSmokeClouds();
        }
    }
}
