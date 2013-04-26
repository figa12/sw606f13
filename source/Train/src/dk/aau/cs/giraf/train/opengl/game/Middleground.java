package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.RenderableMatrix;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Middleground extends GameDrawable {
    
    public Middleground(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }
    
    private final class HillItem {
        public float x;
        public float y;
        public Texture hill;
        
        public HillItem(float x, float y, Texture hill) {
            this.x = x;
            this.y = y;
            this.hill = hill;
        }
    }

    private RenderableMatrix sequence = new RenderableMatrix();
  //  private Square square = new Square(100f, 100f);
    private Texture hill_small = new Texture(1400f, 300f);
    private Texture hill_medium = new Texture(2200f, 460f);
    private Texture hill_large = new Texture(3000f, 600f);
    private Texture hill_larger = new Texture(3800f, 700f);

    
    @Override
    public void load() {
    	
        this.hill_small.loadTexture(super.gl, super.context, R.drawable.texture_hill_small);
        this.hill_medium.loadTexture(super.gl, super.context, R.drawable.texture_hill_medium);
        this.hill_large.loadTexture(super.gl, super.context, R.drawable.texture_hill_large);
        this.hill_larger.loadTexture(super.gl, super.context, R.drawable.texture_hill_larger);

        HillItem[] hillSequence1 = {
        		new HillItem(0f, this.hill_large.getHeight(), this.hill_large),
        		new HillItem(this.hill_large.getWidth()+11f, this.hill_medium.getHeight(), this.hill_medium),
        		new HillItem(this.hill_large.getWidth()/100f*60f, this.hill_small.getHeight(), this.hill_small),
        		new HillItem(this.hill_large.getWidth()+11f + this.hill_medium.getWidth()/100f*65f, this.hill_larger.getHeight(), this.hill_larger)

        }; 
        
        HillItem[] hillSequence2 = {
        		new HillItem(0f, this.hill_small.getHeight(), this.hill_small),
        		new HillItem(this.hill_small.getWidth()/100f*85f + this.hill_large.getWidth()/100f*60f, this.hill_medium.getHeight(), this.hill_medium),
        		new HillItem(this.hill_small.getWidth()/100f*85f, this.hill_large.getHeight(), this.hill_large),        		
        		new HillItem(this.hill_small.getWidth()/100f*85f + this.hill_large.getWidth()/100f*85f, this.hill_larger.getHeight(), this.hill_larger)

        }; 
        
        HillItem[] hillSequence3 = {
        		new HillItem(this.hill_larger.getWidth()/100f*70, this.hill_medium.getHeight(), this.hill_medium),
        		new HillItem(0f, this.hill_larger.getHeight(), this.hill_larger),
        		new HillItem(this.hill_larger.getWidth() + 60f, this.hill_larger.getHeight(), this.hill_larger),        		


        }; 
        
        HillItem[] hillSequence4 = {
        		new HillItem(this.hill_medium.getWidth() + 200f, this.hill_medium.getHeight(), this.hill_medium),
        		new HillItem(this.hill_medium.getWidth() + 150f + this.hill_medium.getWidth()/100f*60f, this.hill_larger.getHeight(), this.hill_larger),
        		new HillItem(this.hill_medium.getWidth()/100f*60f, this.hill_large.getHeight(), this.hill_large),        		
        		new HillItem(0, this.hill_medium.getHeight(), this.hill_medium)

        }; 
        
        HillItem[][] hillSequences = {hillSequence1, hillSequence2, hillSequence3, hillSequence4};
        
        int f;
        float offset;
       	float screenHeight = GlRenderer.getActualHeight(GameData.MIDDLEGROUND);
       	float screenWidth = GlRenderer.getActualWidth(screenHeight);
       	
        this.sequence.addCoordinate(-screenWidth/2, (-screenHeight/2)+39.75f, GameData.MIDDLEGROUND);
    	  
        
        for (float i = 0f; i <= super.gameData.getTotalTravelDistance(GameData.MIDDLEGROUND); i += getSequenceWidth(hillSequences[f]) - 100f) {  
        	f = super.gameDrawer.getRandomNumber(0, hillSequences.length-1);
        	
        	for (HillItem hillItem : hillSequences[f]){
        		this.sequence.addRenderableMatrixItem(hillItem.hill, new Coordinate(i + hillItem.x, hillItem.y, 0f));
        	}        	
		}              
    }
      
    private float getSequenceWidth(HillItem[] hillSequence) {
    	float highestX = hillSequence[0].x + hillSequence[0].hill.getWidth();
    	
    	for (HillItem hillItem : hillSequence){
    		if (hillItem.x + hillItem.hill.getWidth() > highestX) {
    			highestX = hillItem.x + hillItem.hill.getWidth();
    		}
    	}
      	
    	return highestX;
    }

    @Override
    public void draw() {
        this.sequence.move(super.gameData.pixelMovementForThisFrame, 0f);
        super.translateAndDraw(this.sequence);
    }
}


