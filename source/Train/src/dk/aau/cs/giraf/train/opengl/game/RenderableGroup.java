package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.Coordinate;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Renderable;
import dk.aau.cs.giraf.train.opengl.Shape;

/**
 * An abstract class specifying a group of renderables to be drawn and loaded together.
 * Methods for translating, rotating, and drawing.
 * @author Jesper
 */
public abstract class RenderableGroup {
    
    protected GL10 gl;
    protected Context context;
    protected GameDrawer gameDrawer;
    
    public RenderableGroup(GL10 gl, Context context, GameDrawer gameDrawer) {
        this.gl = gl;
        this.context = context;
        this.gameDrawer = gameDrawer;
    }
    
    /** Load everything up. Loading happens one time, before drawing. */
    public abstract void load();
    /** Draw the group */
    public abstract void draw();
    
    /**
     * Translate to each of the renderable's coordinates and draw it.
     * @param renderable is the renderable to be drawn.
     */
    public final void translateAndDraw(Renderable renderable) {
        this.translateAndDraw(renderable, Color.White);
    }
    
    /**
     * Translate to each of the renderable's coordinates and draw it.
     * @param renderable is the renderable to be drawn.
     * @param color is the overlay to be used.
     */
    public final void translateAndDraw(Renderable renderable, Color color) {
        for (Coordinate coordinate : renderable.getCoordinates()) {
            this.translateAndDraw(renderable, coordinate, color);
        }
    }
    
    /**
     * Translate to the specified coordinate and draw the renderable.
     * @param renderable is the renderable to be drawn.
     * @param coordinate is where the renderable will be drawn.
     */
    public final void translateAndDraw(Renderable renderable, Coordinate coordinate) {
        this.translateAndDraw(renderable, coordinate, Color.White);
    }
    
    /**
     * Translate to the specified coordinate and draw the renderable.
     * @param renderable is the renderable to be drawn.
     * @param coordinate is where the renderable will be drawn.
     * @param color is the overlay to be used.
     */
    public final void translateAndDraw(Renderable renderable, Coordinate coordinate, Color color) {
        gameDrawer.moveTo(coordinate);
        renderable.draw(this.gl, coordinate, color);
    }
    
    /**
     * Translate to each of the shape's coordinates, rotate, and draw it.
     * @param angle is the amount to rotate.
     * @param shape is the shape to be rotated/drawn
     */
    public final void translateRotateAndDraw(float angle, Shape shape) {
        this.translateRotateAndDraw(angle, shape, Color.White);
    }
    
    /**
     * Translate to each of the shape's coordinates, rotate, and draw it.
     * @param angle is the amount to rotate.
     * @param shape is the shape to be rotated/drawn
     * @param color is the overlay to be used.
     */
    public final void translateRotateAndDraw(float angle, Shape shape, Color color) {
        for (Coordinate coordinate : shape.getCoordinates()) {
            gameDrawer.moveTo(coordinate);
            shape.rotateCenterAndDraw(this.gl, coordinate, angle, color);
        }
    }
}
