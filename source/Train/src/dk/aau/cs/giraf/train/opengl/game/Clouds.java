package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Clouds extends GameDrawable {
    
    public Clouds(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private final int numberOfClouds = 5;
    
    private final Texture large_cloud = new Texture(800f, 450f);
    private final Texture medium_cloud = new Texture(600f, 350f);
    private final Texture small_cloud = new Texture(400f, 250f);
    private final Texture[] cloudArray = {large_cloud, medium_cloud, small_cloud};
    private Coordinate[] coordinates = new Coordinate[this.numberOfClouds];
    private Color[] colors = new Color[this.numberOfClouds];
    
    private final Coordinate startCoordinate = new Coordinate(1400f, 800f, GameData.BACKGROUND);
    
    private int resetIndex = 0;
    private final float timeBetweenClouds = 300f; // ms
    private float timeSinceLastReset = 0f;
    private final float ySpeed = 0.15f;
    private int f;
    
    
    private final void resetOneCloud() {
        //Increment the reset index
        this.resetIndex = ++this.resetIndex % this.numberOfClouds;
        
        //Put cloud back to exhaust
        this.coordinates[this.resetIndex].setCoordinate(this.startCoordinate.getX(), this.startCoordinate.getY());
        this.colors[this.resetIndex].setColor(1f, 1f, 1f, 1f);
    }
    
    private final void updateClouds() {
        //Updates position and alpha channels
        for (int i = 0; i < this.numberOfClouds; i++) {
            //Always move smoke vertically, move smoke horizontally relative to the train speed.
        	this.coordinates[i].moveX(f = super.gameDrawer.getRandomNumber(-1000, 0));
        	//this.coordinates[i].moveY(this.ySpeed * GameData.timeDifference);
            //Fade the smoke
        //    this.colors[i].alpha -= (1f / (this.timeBetweenSmokeClouds * this.numberOfClouds)) * GameData.timeDifference;
        }
    }
    
    @Override
    public final void load() {
        //Load the texture
        this.large_cloud.loadTexture(gl, context, R.drawable.texture_cloud_large, Texture.AspectRatio.BitmapOneToOne);
        this.large_cloud.loadTexture(gl, context, R.drawable.texture_cloud_medium, Texture.AspectRatio.BitmapOneToOne);
        this.large_cloud.loadTexture(gl, context, R.drawable.texture_cloud_small, Texture.AspectRatio.BitmapOneToOne);
  
        //Start conditions:
        for (int i = 0; i < this.numberOfClouds; i++) {
            this.coordinates[i] = new Coordinate(this.startCoordinate.getX(), this.startCoordinate.getY(), this.startCoordinate.getZ());
            this.colors[i] = new Color(1f, 1f, 1f, 0f); // invisible
        }
    }

    @Override
    public final void draw() {
        //Reset one smoke cloud at the given interval
    
        //Draw all smoke clouds.
        for (int i = 0; i < this.numberOfClouds; i++) {
            super.translateAndDraw(this.cloudArray[0], this.coordinates[i], this.colors[i]);
        }
        
        if(!GameData.isPaused) {
            //Update position and alpha channels
            this.updateClouds();
        }
    }
}
