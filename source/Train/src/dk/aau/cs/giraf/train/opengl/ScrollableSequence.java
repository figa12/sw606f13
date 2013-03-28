package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;

public class ScrollableSequence extends Positionable {
	
    /**
     * Extends {@link Positionable} to get new {@link Coordinate} instead of using the {@link Coordinate} inside the {@link Shape} object.
     * This gives the possibility of reusing a Shape that already have its own coordinates.
     * @author Jesper
     * @see ScrollableItem#ScrollableItem(Shape, Coordinate, Color)
     */
	private class ScrollableItem extends Positionable {
		
	    private Shape shape;
	    
		private Color color;
		
		public ScrollableItem(Shape shape, Coordinate coordinate, Color color) {
            this.shape = shape;
            super.addCoordinate(coordinate);
            this.color = color;
        }

        @Override
        public void draw(GL10 gl, Color color) {
            this.shape.draw(gl, color);
            
        }

        @Override
        public void draw(GL10 gl) {
            this.shape.draw(gl);
        }
	}
	
	private ArrayList<ScrollableItem> sequence;
	
	
	public ScrollableSequence() {
        this.sequence = new ArrayList<ScrollableItem>();
	}
	
	public void addScrollableItem(Shape shape, Coordinate coordinate) { // possibility of overriding the shapes own position
		this.addScrollableItem(shape, coordinate, new Color());
	}
	
	public void addScrollableItem(Shape shape, Coordinate coordinate, Color color) { // possibility of overriding the shapes own position
        ScrollableItem scrollableItem = new ScrollableItem(shape, coordinate, color);
        this.sequence.add(scrollableItem);
    }
	
	private float currentX;
    private float currentY;
	
	/** Draw the sequence */
	public void draw(GL10 gl) {
        this.draw(gl, new Color());
    }
	
	/** Draw the sequence */
	@Override
    public void draw(GL10 gl, Color color) {
        this.currentX = 0f;
        this.currentY = 0f;
        
        gl.glPushMatrix();
        
        for (ScrollableItem item : this.sequence) {
            for (Coordinate coordinate : item.getCoordinates()) {
                // calculate how much to move on the axis to get to the item's position
                currentX += coordinate.x - currentX;
                currentY += coordinate.y - currentY;

                gl.glTranslatef(coordinate.x - currentX, coordinate.y - currentY, 0f);
                item.draw(gl, item.color);
            }
        }
        gl.glPopMatrix();
    }
}
