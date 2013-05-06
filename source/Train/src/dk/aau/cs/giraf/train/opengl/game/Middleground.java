
package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.text.StaticLayout;
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
    private Texture hill_small = new Texture(1400f, 300f);
    private Texture hill_medium = new Texture(2200f, 460f);
    private Texture hill_large = new Texture(3000f, 600f);
    private Texture hill_larger = new Texture(3800f, 700f);
    private Texture cow = new Texture(300f, 180f);
    private Texture tree = new Texture(266f, 390f);
    private Texture[] decorationArray = {cow, tree, tree};
    private RenderableMatrix decorationMatrix = new RenderableMatrix();
   	float screenHeight = GlRenderer.getActualHeight(GameData.MIDDLEGROUND);
   	float screenWidth = GlRenderer.getActualWidth(screenHeight);

    
    @Override
    public void load() {
    	
        this.hill_small.loadTexture(super.gl, super.context, R.drawable.texture_hill_small);
        this.hill_medium.loadTexture(super.gl, super.context, R.drawable.texture_hill_medium);
        this.hill_large.loadTexture(super.gl, super.context, R.drawable.texture_hill_large);
        this.hill_larger.loadTexture(super.gl, super.context, R.drawable.texture_hill_larger);
        this.cow.loadTexture(super.gl, super.context, R.drawable.texture_cow);
        this.tree.loadTexture(super.gl, super.context, R.drawable.texture_tree);
            

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
        
        int chooseHillSequence; // which sequence that should be drawn
        int chooseHillToDrawOn; // where should the decoration be drawn (medium_hill, large_hill, larger_hill)
        int drawDecoration; // which decoration should be drawn (cow, tree, plane)
        float cowHeight; // used to determine how much the cow can move on the y-axis
        float treeHeight;  // used to determine how much the tree can move on the y-axis
        float cowWidth = cow.getWidth()/2; // finds the width of the cow
        float treeWidth = tree.getWidth()/2; // finds the width of the tree
        float mediumHillPlacementOffset; // how far can the cow / tree move on the x-axis on the medium hill
        float largeHillPlacementOffset; // how far can the cow / tree move on the x-axis on the large hill
        float largerHillPlacementOffset; // how far can the cow / tree move on the x-axis on the larger hill

       	
        this.sequence.addCoordinate(-screenWidth/2, (-screenHeight/2)+39.75f, GameData.MIDDLEGROUND);
        this.decorationMatrix.addCoordinate(-screenWidth/2, (-screenHeight/2)+39.75f, GameData.MIDDLEGROUND);  
 
        for (float i = 0f; i <= super.gameData.getTotalTravelDistance(GameData.MIDDLEGROUND); i += getSequenceWidth(hillSequences[chooseHillSequence]) - 100f) {  
        	
        	chooseHillSequence = super.gameDrawer.getRandomNumber(0, hillSequences.length-1); // randomly choosewhich hill sequence to draw
    		drawDecoration = super.gameDrawer.getRandomNumber(0, 2); // Randomly chosen which decoration to draw (0 = cow, 1 = tree, 2 = 2)
    		chooseHillToDrawOn = super.gameDrawer.getRandomNumber(1, 4); // randomly choose which hill to draw on (1 = small hill, 2 = medium hill, 3 = large hill, 4 = larger hill)
    		
        	for (HillItem hillItem : hillSequences[chooseHillSequence]){
        		this.sequence.addRenderableMatrixItem(hillItem.hill, new Coordinate(i + hillItem.x, hillItem.y, 0f));
        		
        		if (decorationArray[drawDecoration] == cow) {
        			
        			switch (chooseHillToDrawOn) {
						case 1:
							if (hillItem.hill == hill_medium) {
								
				        		mediumHillPlacementOffset  = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/5f); // randomly select the coordinate on the x-axis
				        		cowHeight = super.gameDrawer.getRandomNumber(50, cow.getHeight() - 30f); // randomly select the coordinate on the y-axis

				        		// draw the cow on a medium hill				        		
								this.decorationMatrix.addRenderableMatrixItem(cow, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + cowWidth - mediumHillPlacementOffset, hillItem.hill.getHeight() + cowHeight, 0f));
							}
							
							break;
						
						case 2:
							if (hillItem.hill == hill_large) {
								
				        		largeHillPlacementOffset  = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/5f); // randomly select the coordinate on the x-axis
				        		cowHeight = super.gameDrawer.getRandomNumber(50, cow.getHeight() - 40f); // randomly select the coordinate on the y-axis

				        		// draw the cow on a large hill
								this.decorationMatrix.addRenderableMatrixItem(cow, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + cowWidth - largeHillPlacementOffset, hillItem.hill.getHeight() + cowHeight, 0f));
							}
							
							break;

						case 3:
							if (hillItem.hill == hill_larger) {
								
				        		largerHillPlacementOffset  = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/5f); // randomly select the coordinate on the x-axis
				        		cowHeight = super.gameDrawer.getRandomNumber(50, cow.getHeight() - 55f); // randomly select the coordinate on the y-axis
				        		
				        		// draw the cow on a larger hill
								this.decorationMatrix.addRenderableMatrixItem(cow, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + cowWidth - largerHillPlacementOffset, hillItem.hill.getHeight() + cowHeight, 0f));
							}
							
							break;	
						
						default:
							break;
        			}
				}
        		
        		if (decorationArray[drawDecoration] == tree) {
        			
        			switch (chooseHillToDrawOn) {		
        			
						case 1:
							if (hillItem.hill == hill_small) {
							
								treeHeight = tree.getHeight() - 70f; // randomly select the coordinate on the y-axis
	
								// draw the trees on a small hill
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth , hillItem.hill.getHeight() + treeHeight, 0f));
							}	
					
							break;
        				
        			
						case 2:
							if (hillItem.hill == hill_medium) {
								
				        		mediumHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/20f); // randomly select the coordinate on the x-axis
				        		treeHeight = tree.getHeight() - 70f; // randomly select the coordinate on the y-axis
		
				        		// draw the trees on a medium hill
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth*3 - mediumHillPlacementOffset, hillItem.hill.getHeight() + treeHeight, 0f));
								
								mediumHillPlacementOffset  = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/20f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + treeWidth + mediumHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 20f, 0f));
								
							}	
						
							break;
        			
						case 3:
							if (hillItem.hill == hill_large) {
								
				        		largeHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/10f); // randomly select the coordinate on the x-axis
				        		treeHeight = tree.getHeight() - 100; // randomly select the coordinate on the y-axis
								
				        		// draw the tree on a large hill
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth*3 - largeHillPlacementOffset, hillItem.hill.getHeight() + treeHeight, 0f));
								
								largeHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/10f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + treeWidth + largeHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 10f, 0f));
								
								largeHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/4f);
							
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth*3 - largeHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 170f, 0f));
								
								largeHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/4f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + treeWidth + largeHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 170f, 0f));								
							}
							
							break;

						case 4:
							if (hillItem.hill == hill_larger) {
								
				        		largerHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/5f); // randomly select the coordinate on the x-axis
				        		treeHeight = tree.getHeight() - 200; // select the coordinate on the y-axis
								
				        		// draw the tree on a larger hill
				        		this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth - largerHillPlacementOffset , hillItem.hill.getHeight() + treeHeight, 0f));
								
								largerHillPlacementOffset  = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/4f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth + largerHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 10f, 0f));
								
							    largerHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/3.2f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth*1.8f + largerHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 30f, 0f));
								
								largerHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/3.2f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - largerHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 50f, 0f));
							
					     		largerHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/2.7f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) - treeWidth/2 + largerHillPlacementOffset, hillItem.hill.getHeight() + treeHeight - 200f, 0f));
								
								largerHillPlacementOffset = super.gameDrawer.getRandomNumber(0, hillItem.hill.getWidth()/2.5f);
								
								this.decorationMatrix.addRenderableMatrixItem(tree, new Coordinate(i + hillItem.x + (hillItem.hill.getWidth()/2f) + treeWidth/2 - largerHillPlacementOffset/2, hillItem.hill.getHeight() + treeHeight - 200f, 0f));

							}
							
							break;	
						
						default:
							
							break;
        			}		
        		}
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
        this.sequence.move(super.gameData.getPixelMovement(), 0f);
        this.decorationMatrix.move(super.gameData.getPixelMovement(), 0f);
        super.translateAndDraw(this.sequence);
        super.translateAndDraw(this.decorationMatrix);

    }
}


