package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Texture;
import dk.aau.cs.giraf.train.opengl.game.RenderableGroup;

public final class TrainSmoke extends RenderableGroup { //FIXME if numberOfSmokeClouds should change according to 
    
    public TrainSmoke(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private int numberOfSmokeClouds = 10;
    
    private Texture smokeCloud = new Texture(1f, 1f);
    private Coordinate[] coordinates = new Coordinate[this.numberOfSmokeClouds];
    private Color[] colors = new Color[this.numberOfSmokeClouds];
    
    private Coordinate startCoordinate = new Coordinate(455.42f, -52.37f, super.gameDrawer.FOREGROUND);
    
    private int resetIndex = 0;
    private float timeBetweenSmokeParticles = 80f; // ms
    private float timeSinceLastReset = 0f;
    private final float ySpeed = 0.18f;
    
    private void resetOneSmokeCloud() {
        this.resetIndex = ++this.resetIndex % this.numberOfSmokeClouds;
        
        this.coordinates[this.resetIndex].setCoordinate(this.startCoordinate.getX(), this.startCoordinate.getY());
        this.colors[this.resetIndex].setColor(Color.TrainSmoke.red, Color.TrainSmoke.green, Color.TrainSmoke.blue, Color.TrainSmoke.alpha);
    }
    
    private int i; // if i'm not mistake;, allocate permanent memory. Not so much garbage.
    
    private void updateSmokeClouds() {
        for (i = 0; i < this.numberOfSmokeClouds; i++) {
            this.coordinates[i].moveX(super.gameDrawer.pixelMovementForThisFrame);
            this.coordinates[i].moveY(this.ySpeed*super.gameDrawer.timeDifference);
            
            this.colors[i].alpha -= (1f / (this.timeBetweenSmokeParticles * this.numberOfSmokeClouds)) * super.gameDrawer.timeDifference;
        }
    }
    
    @Override
    public void load() {
        this.smokeCloud.loadTexture(gl, context, R.drawable.texture_train_smoke, Texture.AspectRatio.BitmapOneToOne);
        
        /* Start conditions. */
        for (i = 0; i < this.numberOfSmokeClouds; i++) {
            this.coordinates[i] = new Coordinate(this.startCoordinate.getX(), this.startCoordinate.getY(), this.startCoordinate.getZ());
            this.colors[i] = new Color(Color.TrainSmoke.red, Color.TrainSmoke.green, Color.TrainSmoke.blue, 0f); // invisible
        }
    }

    @Override
    public void draw() {
        this.timeSinceLastReset += super.gameDrawer.timeDifference;
        if(this.timeSinceLastReset >= this.timeBetweenSmokeParticles) {
            this.timeSinceLastReset = 0f;
            this.resetOneSmokeCloud();
        }
        
        /* Draw all smoke clouds. */
        for (i = 0; i < this.numberOfSmokeClouds; i++) {
            super.translateAndDraw(this.smokeCloud, this.coordinates[i], this.colors[i]);
        }
        
        this.updateSmokeClouds();
    }
}
