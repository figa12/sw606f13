package dk.aau.cs.giraf.train.opengl;

/**
 * Simple class holding a {@code x}, {@code y}, and {@code z} coordinate.
 * @author Jesper
 * @see #Coordinate(float, float, float)
 */
public class Coordinate {
    private float x;
    private float y;
    private float z;
    
    private float visibleWidth;
    private float visibleHeight;
    
    /**
     * Creates a new coordinate container with the specified coordinate.
     * @param x
     * @param y
     * @param z
     */
    public Coordinate(float x, float y, float z) {
        this.setCoordinate(x, y, z);
    }
    
    /** Get the x attribute. */
    public float getX() {
        return this.x;
    }
    
    /** Get the y attribute. */
    public float getY() {
        return this.y;
    }
    
    /** Get the z attribute. */
    public float getZ() {
        return this.z;
    }
    
    /**
     * Move the coordinate on the x-axis.
     * @param x
     */
    public void moveX(float x) {
        this.x += x;
    }
    
    /**
     * Move the coordinate on the y-axis.
     * @param y
     */
    public void moveY(float y) {
        this.y += y;
    }
    
    /**
     * Move the coordinate on the z-axis.
     * @param z
     */
    public void moveZ(float z) {
        this.z += z;
        this.calculateVisibility(); // recalculate
    }
    
    /** Get the visisble screen width in the depth of this coordinate. */
    public float getVisibleWidth() {
        return this.visibleWidth;
    }
    
    /** Get the visisble screen height in the depth of this coordinate. */
    public float getVisibleHeight() {
        return this.visibleHeight;
    }
    
    /** Calculate the visible screen width/height in the depth of this coordinate. */
    private void calculateVisibility() {
        this.visibleHeight = GlRenderer.getActualHeight(this.z);
        this.visibleWidth = GlRenderer.getActualWidth(this.visibleHeight);
    }
    
    /**
     * Set the coordinate.
     * @param x
     * @param y
     */
    public void setCoordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Set the coordinate.
     * @param x
     * @param y
     * @param z
     */
    public void setCoordinate(float x, float y, float z) {
        this.setCoordinate(x, y);
        this.z = z;
        this.calculateVisibility();
    }
    
    /** Reset the coordinate to (0,0,0) */
    public void resetCoordinate() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
        
        this.visibleHeight = 0f;
        this.visibleWidth = 0f;
    }
}
