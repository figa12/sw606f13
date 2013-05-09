package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

/**
 * An abstract class for objects that can be rendered in OpenGL.
 * Renderables must implement a draw method. The draw method should draw the Renderable at the {@link Coordination#coordinates}.
 * @author Jesper Riemer Andersen
 * @see #draw(GL10)
 * @see #draw(GL10, Color)
 * @see Coordination
 */
public abstract class Renderable extends Coordination {
    
    /**
     * Draw this renderable.
     * @param gl the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     */
    public abstract void draw(GL10 gl, Coordinate coordinate);
    
    /**
     * Draw this renderable.
     * @param gl the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color overlay
     */
    public abstract void draw(GL10 gl, Coordinate coordinate, Color color);
    
    /**
     * Get the width of this {@link Renderable}.
     * @return The Width.
     */
    public abstract float getWidth();
    
    /** 
     * Get the height of this {@link Renderable}.
     * @return The Height.
     */
    public abstract float getHeight();
    
}
