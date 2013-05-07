package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

/**
 * A matrix of renderables.
 * Use of the z-axis in this matrix is disabled.
 * @author Jesper Riemer Andersen
 * @see Renderable
 */
public class RenderableMatrix extends Renderable {
	
    /**
     * Extends {@link Renderable} to get a new {@link Coordinate},
     * instead of using the {@link Coordinate}s inside the contained {@link Renderable} object.
     * This gives the possibility of reusing a renderable that already have its own coordinates.
     * @author Jesper
     * @see #RenderableMatrixItem(Renderable, Coordinate, Color)
     */
	private class RenderableMatrixItem extends Renderable {
		
	    /** The contained renderable. */
	    private Renderable renderable;
	    /** The contained color to overlay the renderable. */
		private Color color;
		
		/**
		 * Create a new {@link RenderableMatrixItem}.
		 * @param renderable to be drawn in the matrix
		 * @param coordinate where to draw the renderable
		 * @param color the overlay
		 */
		public RenderableMatrixItem(Renderable renderable, Coordinate coordinate, Color color) {
            this.renderable = renderable;
            super.addCoordinate(coordinate);
            this.color = color;
        }
		
		@Override
        public float getWidth() {
            return this.renderable.getWidth();
        }

        @Override
        public float getHeight() {
            return this.renderable.getHeight();
        }
		
		@Override
        public void draw(GL10 gl, Coordinate coordinate) {
		    // always use the this.color
            this.renderable.draw(gl, coordinate, this.color);
        }
		
        @Override
        public void draw(GL10 gl, Coordinate coordinate, Color color) {
            // ignore color input, use this.color instead
            this.renderable.draw(gl, coordinate, this.color);
        }
	}
	
	/** The {@link ArrayList} of {@link RenderableMatrixItem} in this matrix. */
	private ArrayList<RenderableMatrixItem> matrixItems = new ArrayList<RenderableMatrixItem>();
	
	/**
	 * Add a {@link RenderableMatrixItem} to this matrix.
	 * The {@link Renderable}'s own coordinates are ignored, the input coordinate is used instead.
	 * The z-axis is disabled.
	 * @param renderable to add to this matrix.
	 * @param coordinate in this matrix where the renderable is to be drawn.
	 */
	public void addRenderableMatrixItem(Renderable renderable, Coordinate coordinate) {
		this.addRenderableMatrixItem(renderable, coordinate, Color.White);
	}
	
	/**
	 * Add a {@link RenderableMatrixItem} to this matrix.
	 * The {@link Renderable}'s own coordinates are ignored, the input coordinate is used instead.
	 * The z-axis is disabled.
	 * @param renderable to add to this matrix.
     * @param coordinate in this matrix where the renderable is to be drawn.
	 * @param color overlay, in this matrix, for this item.
	 */
	public void addRenderableMatrixItem(Renderable renderable, Coordinate coordinate, Color color) {
        RenderableMatrixItem scrollableItem = new RenderableMatrixItem(renderable, coordinate, color);
        this.matrixItems.add(scrollableItem);
    }
	
	@Override
    public float getWidth() {
        if(this.matrixItems.size() == 0) {
            return 0f;
        }
        
	    float highestX = this.matrixItems.get(0).getCoordinates().get(0).getX();
        
        for (RenderableMatrixItem item : this.matrixItems) {
            for (Coordinate itemCoordinate : item.getCoordinates()) {
                if(highestX < itemCoordinate.getX() + item.getWidth()) {
                    highestX = itemCoordinate.getX() + item.getWidth();
                }
            }
        }
        // in this case the width is defined by the distance from the matrix center to the farthest item + width
        return highestX;
    }

    @Override
    public float getHeight() {
        if(this.matrixItems.size() == 0) {
            return 0f;
        }
        
        float highestY = this.matrixItems.get(0).getCoordinates().get(0).getY();
        
        for (RenderableMatrixItem item : this.matrixItems) {
            for (Coordinate itemCoordinate : item.getCoordinates()) {
                if(highestY > itemCoordinate.getY() - item.getHeight()) {
                    highestY = itemCoordinate.getY() - item.getHeight();
                }
            }
        }
        // in this case the height is defined by the distance from the matrix center to the lowest item + height
        return Math.abs(highestY);
    }
	
	/** The current X position inside this matrix. */
	private float currentX;
	/** The current Y position inside this matrix. */
    private float currentY;
	
    /**
     * Move inside this matrix.
     * Moving on the z-axis is not allowed.
     * @param gl the {@link GL10} instance.
     * @param x amount to move.
     * @param y amount to move.
     */
    private void translate(GL10 gl, float x, float y) {
        currentX += x;
        currentY += y;
        
        gl.glTranslatef(x, y, 0f);
    }
    
    /**
     * Move to the the specified coordinates.
     * @param gl the {@link GL10} instance.
     * @param coordinate to move to.
     * @see Coordinates
     */
    private void translateTo(GL10 gl, Coordinate coordinate) {
        this.translate(gl, coordinate.getX() - currentX, coordinate.getY() - currentY);
    }
    
    /**
     * Draw the entire renderable matrix.
     * @param gl the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     */
    @Override
	public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
	
    /**
     * Draw the entire renderable matrix.
     * @param gl the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color is the overlay.
     */
	@Override
    public void draw(GL10 gl, Coordinate coordinate, Color color) {
        this.currentX = 0f;
        this.currentY = 0f;
        
        gl.glPushMatrix();
        
        //These for-loops create less garbage than the foreach counterpart
        for (int i = 0; i < this.matrixItems.size(); i++) {
            ArrayList<Coordinate> coordinates = this.matrixItems.get(i).getCoordinates();
            for (int j = 0; j < coordinates.size(); j++) {
                //only draw if the item is visible on the screen
                if(     coordinate.getX() + coordinates.get(j).getX() + this.matrixItems.get(i).getWidth() >= -coordinate.getVisibleWidth()/2 &&
                        coordinate.getX() + coordinates.get(j).getX() <= coordinate.getVisibleWidth()/2 &&
                        coordinate.getY() + coordinates.get(j).getY() >= -coordinate.getVisibleHeight()/2 &&
                        coordinate.getY() + coordinates.get(j).getY() - this.matrixItems.get(i).getHeight() <= coordinate.getVisibleHeight()/2 ) {
                    
                    this.translateTo(gl, coordinates.get(j));
                    this.matrixItems.get(i).draw(gl, coordinates.get(j));
                }
            }
        }
        
        gl.glPopMatrix();
    }
}
