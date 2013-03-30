package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;

/**
 * A matrix of renderables.
 * Use of the z-axis in this matrix is disabled.
 * @author Jesper
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
        public void draw(GL10 gl) {
		    // always use the this.color
            this.renderable.draw(gl, this.color);
        }
		
        @Override
        public void draw(GL10 gl, Color color) {
            // ignore color input, use this.color instead
            this.renderable.draw(gl, this.color);
        }
	}
	
	/** The {@link ArrayList} of {@link RenderableMatrixItem} in this matrix. */
	private ArrayList<RenderableMatrixItem> matrixItems = new ArrayList<RenderableMatrixItem>();
	
	/**
	 * Add a {@link RenderableMatrixItem} to this matrix.
	 * The {@link Renderable}'s own coordinates are ignored, the input coordinate is used instead.
	 * @param renderable to add to this matrix.
	 * @param coordinate in this matrix where the renderable is to be drawn.
	 */
	public void addRenderableMatrixItem(Renderable renderable, Coordinate coordinate) {
		this.addRenderableMatrixItem(renderable, coordinate, Colors.White);
	}
	
	/**
	 * Add a {@link RenderableMatrixItem} to this matrix.
	 * The {@link Renderable}'s own coordinates are ignored, the input coordinate is used instead.
	 * @param renderable to add to this matrix.
     * @param coordinate in this matrix where the renderable is to be drawn.
	 * @param color overlay, in this matrix, for this item.
	 */
	public void addRenderableMatrixItem(Renderable renderable, Coordinate coordinate, Color color) {
        RenderableMatrixItem scrollableItem = new RenderableMatrixItem(renderable, coordinate, color);
        this.matrixItems.add(scrollableItem);
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
    private void move(GL10 gl, float x, float y) {
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
    private void moveTo(GL10 gl, Coordinate coordinate) {
        this.move(gl, coordinate.x - currentX, coordinate.y - currentY);
    }
    
    /**
     * Draw the entire renderable matrix.
     * @param gl the {@link GL10} instance.
     */
    @Override
	public void draw(GL10 gl) {
        this.draw(gl, Colors.White);
    }
	
    /**
     * Draw the entire renderable matrix.
     * @param gl the {@link GL10} instance.
     * @param color is the overlay.
     */
	@Override
    public void draw(GL10 gl, Color color) {
        this.currentX = 0f;
        this.currentY = 0f;
        
        gl.glPushMatrix();
        
        for (RenderableMatrixItem item : this.matrixItems) {
            for (Coordinate coordinate : item.getCoordinates()) {
                this.moveTo(gl, coordinate);
                item.draw(gl);
            }
        }
        gl.glPopMatrix();
    }
}
