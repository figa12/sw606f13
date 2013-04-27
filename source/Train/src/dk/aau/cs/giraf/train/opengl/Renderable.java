package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * An abstract class for objects that can be rendered in OpenGL.
 * Renderables must implement a draw method. The draw method should draw the Renderable at the {@link #coordinates}
 * @author Jesper Riemer Andersen
 * @see #draw(GL10)
 * @see #draw(GL10, Color)
 */
public abstract class Renderable {
    
    /** The coordinates where this {@link Renderable} should be drawn. */
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    
    /** Get the {@link #coordinates} for this {@link Renderable}. */
    public ArrayList<Coordinate> getCoordinates() {
        return this.coordinates;
    }
    
    /**
     * Add a coordinate to this {@link Renderable}.
     * @param coordinate
     * @see Coordinate
     * @see #addCoordinate(float, float, float)
     */
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }
    
    /**
     * Add a coordinate to this {@link Renderable}.
     * @param x
     * @param y
     * @param z
     * @see #addCoordinate(Coordinate)
     */
    public void addCoordinate(float x, float y, float z) {
        this.coordinates.add(new Coordinate(x, y, z));
    }
    
    /**
     * Move all the {@link #coordinates} by the specified amount.
     * @param moveX
     * @param moveY
     * @see Coordinate
     */
    public void move(float moveX, float moveY) {
        for (Coordinate coordinate : this.coordinates) {
            coordinate.moveX(moveX);
            coordinate.moveY(moveY);
        }
    }
    
    /**
     * Move all the {@link #coordinates} by the specified amount.
     * @param moveX
     * @param moveY
     * @param moveZ
     * @see Coordinate
     */
    public void move(float moveX, float moveY, float moveZ) {
        for (Coordinate coordinate : this.coordinates) {
            coordinate.moveX(moveX);
            coordinate.moveY(moveY);
            coordinate.moveZ(moveZ);
        }
    }
    
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
    
    /** Return the width of this {@link Renderable}. */
    public abstract float getWidth();
    
    /** Return the height of this {@link Renderable}. */
    public abstract float getHeight();
    
}
