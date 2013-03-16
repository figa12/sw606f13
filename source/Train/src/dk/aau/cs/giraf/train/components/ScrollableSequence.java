package dk.aau.cs.giraf.train.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;

public class ScrollableSequence {
	
	public abstract class ScrollableItem {
		/** The position on the sequence of this item */
	    private float position = 0.0f;
		
	    /** The y coordinate on the sequence's height */
		private float y = 0.0f;
		
		
		public abstract float getWidth();
		
		public abstract float getHeight();
		
		/** Draw the item */
		public abstract void draw();
		
		
		/** Gets the current x-coordinate */
		public float getX() {
			return this.position;
		}
		
		/** Gets the current y-coordinate */
		public float getY() {
			return this.y;
		}
		
		/** Determine whether this item has already visited the screen */
		public boolean hasFinishedVisitingScreen(float currentSequencePosition) {
			return (this.position + this.getWidth() < currentSequencePosition) ? true : false;
		}
	}
	
	public class BitmapContainer extends ScrollableItem {
		private Bitmap bitmap = null;
		
		public BitmapContainer(Bitmap bitmap, int height) {
			// Scale the width to fit the height
			int width = (int)(this.getWidth() * (float)height/this.getHeight());
			
			// TODO implement
		}
		
		public Bitmap getBitmap() {
			return this.bitmap;
		}
		
		@Override
		public float getWidth() {
			return this.bitmap.getWidth();
		}
		
		@Override
		public float getHeight() {
			return this.bitmap.getHeight();
		}

		@Override
		public void draw() {
			//TODO draw the item at the specified location
		}
	}
	
	public class Color extends ScrollableItem {
	    
        @Override
        public float getWidth() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public float getHeight() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void draw() {
            // TODO Auto-generated method stub
            
        }
	}
	
	
	/** The image sequence to scroll over the screen in the order of the list */
	private List<ScrollableItem> sequence;
	
	/** The index of the right most element of the current elements on the screen */
	private int currentIndex = 0;
	
	/** The list containing the current elements on the screen */
	private ArrayList<ScrollableItem> current;
	
	private float heightOfTheSequence;
	
	private float screenWidth;
	
	private float screenHeight;
	
	private float length;
	
	private float currentSequencePosition = 0;
	
	
	public ScrollableSequence(float screenWidth, float screenHeight, float heightOfTheSequence, float length) {
		this(screenWidth, screenHeight, heightOfTheSequence, length, new ArrayList<ScrollableItem>());
	}
	
	public ScrollableSequence(float screenWidth, float screenHeight, float heightOfTheSequence, float length, List<ScrollableItem> sequence) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.heightOfTheSequence = heightOfTheSequence;
		this.length = length;
		this.sequence = sequence;
		
		this.current = new ArrayList<ScrollableSequence.ScrollableItem>();
	}
	
	public void addItem(ScrollableItem item) {
		this.sequence.add(item);
		
		if(this.sequence.size() != 0) {
			this.current.add(item);
		}
	}
	
	/** Move the sequence horizontally by the value x, and remove items from the current list that no longer appear on screen */
	public void moveSequenceHorizontally(float x) {
	    // TODO update currentSequencePosition and update the current list
	}
	
	/** Get the current elements that appears on screen; total or partial */
	public ArrayList<ScrollableItem> getCurrent() {
		return this.current;
	}
	
	/** Get the next element from the sequence, if the current index is the last one, then restart the sequence */
	private ScrollableItem getNext() {
		this.currentIndex = ++this.currentIndex % this.sequence.size();
		return this.sequence.get(this.currentIndex);
	}
	
	/** Save the screen size, remember to also call saveScreenSize() when the screen size changes */
	public void saveScreenSize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}
	
	/** Draw this sequence's current progress */
	public void drawAll() {
		for (ScrollableItem item : this.current) {
			item.draw();
		}
	}
}
