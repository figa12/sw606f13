package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;

/**
 * //TODO
 * @author Jesper
 *
 */
public class ScrollableSequence extends Renderable {
	
    /**
     * Extends {@link Renderable} to get a new {@link Coordinate} instead of using the {@link Coordinate} inside the {@link Shape} object.
     * This gives the possibility of reusing a Shape that already have its own coordinates.
     * @author Jesper
     * @see ScrollableItem#ScrollableItem(Shape, Coordinate, Color)
     */
	private class ScrollableItem extends Renderable {
		
	    private Shape shape;
	    
		private Color color;
		
		public ScrollableItem(Shape shape, Coordinate coordinate, Color color) {
            this.shape = shape;
            super.addCoordinate(coordinate);
            this.color = color;
        }

		@Override
        public void draw(GL10 gl) {
		    // always use the this.color
            this.shape.draw(gl, this.color);
        }
		
        @Override
        public void draw(GL10 gl, Color color) {
            // ignore color input, use this.color instead
            this.shape.draw(gl, this.color);
        }
	}
	
	private ArrayList<ScrollableItem> sequence = new ArrayList<ScrollableItem>();
	
	
	public void addScrollableItem(Shape shape, Coordinate coordinate) { // possibility of overriding the shapes own position
		this.addScrollableItem(shape, coordinate, Colors.White);
	}
	
	public void addScrollableItem(Shape shape, Coordinate coordinate, Color color) { // possibility of overriding the shapes own position
        ScrollableItem scrollableItem = new ScrollableItem(shape, coordinate, color);
        this.sequence.add(scrollableItem);
    }
	
	private float currentX;
    private float currentY;
	
    private void move(GL10 gl, float x, float y) {
        currentX += x;
        currentY += y;
        
        gl.glTranslatef(x, y, 0f);
    }
    
    private void moveTo(GL10 gl, Coordinate coordinate) {
        this.move(gl, coordinate.x - currentX, coordinate.y - currentY);
    }
    
	/** Draw the sequence */
    @Override
	public void draw(GL10 gl) {
        this.draw(gl, Colors.White);
    }
	
	/** Draw the sequence */
	@Override
    public void draw(GL10 gl, Color color) {
        this.currentX = 0f;
        this.currentY = 0f;
        
        gl.glPushMatrix();
        
        for (ScrollableItem item : this.sequence) {
            for (Coordinate coordinate : item.getCoordinates()) {
                this.moveTo(gl, coordinate);
                item.draw(gl);
            }
        }
        gl.glPopMatrix();
    }
}
