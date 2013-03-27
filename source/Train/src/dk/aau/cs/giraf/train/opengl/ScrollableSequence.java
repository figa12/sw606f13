package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class ScrollableSequence {
	
	private class ScrollableItem {
		
	    private Shape shape;
	    
	    /** The position on the sequence of this item */
	    private float x = 0.0f;
		
	    /** The y coordinate on the sequence's height */
		private float y = 0.0f;
		
		private Color color;
		
		public ScrollableItem(Shape shape, float x, float y) {
		    this(shape, x, y, new Color());
		}
		
		public ScrollableItem(Shape shape, float x, float y, Color color) {
            this.shape = shape;
            this.x = x;
            this.y = y;
            this.color = color;
        }
		
		/** Gets the current x-coordinate */
		/*public float getX() {
			return this.x;
		}*/
		
		/** Gets the current y-coordinate */
		/*public float getY() {
			return this.y;
		}*/
	}
	
	private ArrayList<ScrollableItem> sequence;
	
	
	public ScrollableSequence() {
        this.sequence = new ArrayList<ScrollableItem>();
	}
	
	public void addScrollableItem(Shape shape, float x, float y) {
		ScrollableItem scrollableItem = new ScrollableItem(shape, x, y);
	    this.sequence.add(scrollableItem);
	}
	
	/** Draw the sequence */
    public void draw(GL10 gl) {
        float currentX = 0.0f; //FIXME garbage for garbage collector each frame
        float currentY = 0.0f; //FIXME garbage for garbage collector each frame
        
        gl.glPushMatrix();
        
        for (ScrollableItem item : this.sequence) {
            currentX = item.x - currentX;
            currentY = item.y - currentY;
            gl.glTranslatef(currentX, currentY, 0f);
            item.shape.draw(gl, item.color);
        }
        gl.glPopMatrix();
    }
}
