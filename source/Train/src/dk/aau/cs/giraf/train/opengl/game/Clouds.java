package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.Texture;
import dk.aau.cs.giraf.train.opengl.game.GameDrawable;

public final class Clouds extends GameDrawable {
    
    public Clouds(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }
    
    private class CloudContainer {
    	
    	public float cloudFrequence;
    	public int index;
    	public Coordinate coordinate;
    	public float cloudSpeed;
    	
    	public CloudContainer() {
    		
       	    this.index = 2;
    		this.cloudFrequence = gameDrawer.getRandomNumber(screenWidth + cloudArray[index].getWidth(), (screenWidth + cloudArray[index].getWidth())*1.5f);
    		this.coordinate = new Coordinate(gameDrawer.getRandomNumber(-screenWidth - cloudArray[index].getWidth(), screenWidth/2f), 800f, GameData.BACKGROUND);	 
    		this.cloudSpeed = gameDrawer.getRandomNumber(-5f, -2f);
    	}
    	
    	public void resetCloud() {
    		this.coordinate.setCoordinate(screenWidth/2f, gameDrawer.getRandomNumber(screenHeight/2f - screenHeight/4f + cloudArray[index].getHeight(), screenHeight/2f));
    		this.cloudSpeed = gameDrawer.getRandomNumber(-5f, -2f);
    	}
    	
    	public void updateCloud(){
    		this.coordinate.moveX(cloudSpeed);
    		
    		if(this.coordinate.getX() < (screenWidth/2f) - cloudFrequence) {
    			resetCloud();
    		}
    	}
    }

    private final int numberOfClouds = 5;
   	float screenHeight = GlRenderer.getActualHeight(GameData.BACKGROUND);
   	float screenWidth = GlRenderer.getActualWidth(screenHeight);
    private final Texture large_cloud = new Texture(400f, 200f);
    private final Texture medium_cloud = new Texture(300f, 150f);
    private final Texture small_cloud = new Texture(200f, 100f);
    private Texture[] cloudArray = {small_cloud, medium_cloud, large_cloud};
    private CloudContainer[] cloudContainer = new CloudContainer[this.numberOfClouds]; 
    
    private final void updateClouds() {
        //Updates position and alpha channels
        for (int i = 0; i < this.numberOfClouds; i++) {
            // update all clouds at once
            this.cloudContainer[i].updateCloud();
        }
    }
    
    @Override
    public final void load() {
        //Load the texture
        this.small_cloud.loadTexture(gl, context, R.drawable.texture_cloud_small);
        this.medium_cloud.loadTexture(gl, context, R.drawable.texture_cloud_medium);
        this.large_cloud.loadTexture(gl, context, R.drawable.texture_cloud_large);

        for (int i = 0; i < numberOfClouds; i++) {
			cloudContainer[i] = new CloudContainer(); 
		}
        
    }

    @Override
    public final void draw() {

        //Reset one smoke cloud at the given interval    		
              
        if(!super.gameData.isPaused) {
            //Update position and alpha channels
            this.updateClouds();
        }
        
        //Draw all smoke clouds.       
        for (int i = 0; i < this.numberOfClouds; i++) {
            super.translateAndDraw(this.cloudArray[cloudContainer[i].index], this.cloudContainer[i].coordinate); 
        }
    }
}
