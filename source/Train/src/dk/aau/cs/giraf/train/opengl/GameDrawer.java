package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

import dk.aau.cs.giraf.train.opengl.game.GameData;
import dk.aau.cs.giraf.train.opengl.game.RenderableGroup;
import android.content.Context;

/**
 * This class handles all game drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadGame()
 */
public final class GameDrawer {
    
	/** An enum indicating a type of weather. */
	public enum WeatherStyle {
	    Sunny, Cloudy
	}
	
	private GL10 gl;
	public Coordinate currentPosition = new Coordinate(0f, 0f, 0f);
	public WeatherStyle weatherStyle = WeatherStyle.Sunny;
	private Random random = new Random();
	
	/** The list of {@link RenderableGroup}s. */
	private ArrayList<RenderableGroup> renderableGroups = new ArrayList<RenderableGroup>();
	
	/**
	 * Create the {@link GameDrawer}. All {@link RenderableGroup}s are created here.
	 * @param gl the {@link GL10} instance.
	 * @param context
	 */
	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		
		//Start by creating the stations object, and calculate the stopping positions
		dk.aau.cs.giraf.train.opengl.game.Station station = new dk.aau.cs.giraf.train.opengl.game.Station(gl, context, this);
		station.calculateStoppingPositions();
		
		// add RenderableGroups to the list in the order they should be drawn
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Weather(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Middleground(gl, context, this));
		this.addRenderableGroup(station);
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Train(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.TrainSmoke(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Wheels(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Overlay(gl, context, this));
		
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Tester(gl, context, this)); // Always draw last
	}
	
	private final void addRenderableGroup(RenderableGroup renderableGroup) {
	    //FIXME this method doesn't really do anything anymore, consider removing
		this.renderableGroups.add(renderableGroup);
	}

	/** Draw everything on screen. */
	public synchronized final void drawGame() { //FIXME does it give sense to use 'synchronized' keyword here?
	    this.resetPosition();
	    
	    GameData.systemTimeNow = System.nanoTime();
	    GameData.updateData();
	    
		for (int i = 0; i < this.renderableGroups.size(); i++) {
		    this.renderableGroups.get(i).draw();
        }
		
		GameData.systemTimeLast = GameData.systemTimeNow;
	}

	/** Call all {@link RenderableGroup#load()} from {@link GameDrawer#renderableGroups}. */
	public final void loadGame() {
		for (RenderableGroup renderableGroup : this.renderableGroups) {
			renderableGroup.load();
		}
	}
	
	/**
	 * Moves the current position to the coordinate.
	 * @param coordinate is the position to translate to.
	 * @see #translate(float, float, float)
	 */
	public final void translateTo(Coordinate coordinate) {
	    this.translate(coordinate.getX() - this.currentPosition.getX(),
	              coordinate.getY() - this.currentPosition.getY(),
	              coordinate.getZ() - this.currentPosition.getZ());
	}
	
	/**
	 * Translate by the specified amount.
	 * @param x direction
	 * @param y direction
	 * @param z direction
	 */
	private final void translate(float x, float y, float z) {
	    this.gl.glTranslatef(x, y, z);

        this.currentPosition.moveX(x);
        this.currentPosition.moveY(y);
        this.currentPosition.moveZ(z);
	}
	
	/** Load identity and reset the current coordinate to 0. */
	private final void resetPosition() {
	    this.gl.glLoadIdentity();
	    this.currentPosition.resetCoordinate();
	}
	
	/**
	 * Gets a random number between the specified boundaries.
	 * @param minimum is the minimum value of the random number.
	 * @param maximum is the maximum value of the random number.
	 * @return A random floating point number between minimum and maximum.
	 * @see Random
	 */
	private final float getRandomNumber(float minimum, float maximum) {
	    return minimum + (maximum - minimum) * this.random.nextFloat();
	}
	
	/**
     * Gets a random number between the specified boundaries.
     * @param minimum is the minimum value of the random number.
     * @param maximum is the maximum value of the random number.
     * @return A random integer between minimum and maximum.
     * @see Random
     */
	private final int getRandomNumber(int minimum, int maximum) {
	    return this.random.nextInt(maximum - minimum + 1) + minimum;
	}
}
