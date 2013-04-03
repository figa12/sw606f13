package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.game.RenderableGroup;
import android.content.Context;
import android.util.TimingLogger;

/**
 * This class handles all game drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadTexture()
 */
public class GameDrawer {
    
	/** An enum indicating a type of weather. */
	public enum WeatherStyle {
	    Sunny, Cloudy
	}
	
	private GL10 gl;
	public Coordinate currentPosition = new Coordinate(0f, 0f, 0f);
	public WeatherStyle weatherStyle = WeatherStyle.Sunny;
	private Random random = new Random();
	
	public float currentTrainSpeed = 0.1f; // pixels per ms
	public float pixelMovementForThisFrame = 0f; // timeDifference*currentTrainSpeed //TODO better name.
	
	public final float FOREGROUND = -907.744f;
	public final float MIDDLEGROUND = -1300f;
	
	public float timeDifference;
	private long systemTimeLast = System.nanoTime();
	private long systemTimeNow = 1;
	
	
	/** The list of {@link RenderableGroup}s. */
	private ArrayList<RenderableGroup> gameRenderableGroups = new ArrayList<RenderableGroup>();
	
	/**
	 * Create the {@link GameDrawer}. All {@link RenderableGroup}s are created here.
	 * @param gl the {@link GL10} instance
	 * @param context
	 */
	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		
		// add RenderableGroups to the list in the order they should be drawn
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Middleground(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Station(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Train(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.TrainSmoke(gl, context, this));
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Wheels(gl, context, this));
		
		this.addRenderableGroup(new dk.aau.cs.giraf.train.opengl.game.Tester(gl, context, this)); // Always draw last
	}
	
	/** Add a renderable group to the list of renderable groups. */
	private final void addRenderableGroup(RenderableGroup renderableGroup) {
	    //FIXME this method doesn't really do anything anymore, consider removing
		this.gameRenderableGroups.add(renderableGroup);
	}

	/** Draw everything on screen. */
	public synchronized final void drawGame() { //FIXME does it give sense to use 'synchronized' keyword here?
	    this.resetPosition();
	    
	    this.systemTimeNow = System.nanoTime();
	    this.timeDifference = (this.systemTimeNow - this.systemTimeLast)/1000000.0f; //FIXME follow the trace of this for the first drawn frame. It's dangerous.
	    this.pixelMovementForThisFrame = -this.currentTrainSpeed * this.timeDifference;
	    
		for (RenderableGroup renderableGroup : this.gameRenderableGroups) {
			renderableGroup.draw();
		}
		
		this.systemTimeLast = this.systemTimeNow;
	}

	/** Call all {@link RenderableGroup#load()} from {@link GameDrawer#gameRenderableGroups}. */
	public final void loadTexture() {
		for (RenderableGroup renderableGroup : this.gameRenderableGroups) {
			renderableGroup.load();
		}
	}
	
	/**
	 * Moves the current position to the coordinate.
	 * @param coordinate is the position to translate to.
	 * @see #move(float, float, float)
	 */
	public final void moveTo(Coordinate coordinate) {
	    this.move(coordinate.getX() - this.currentPosition.getX(),
	              coordinate.getY() - this.currentPosition.getY(),
	              coordinate.getZ() - this.currentPosition.getZ());
	}
	
	/**
	 * Translate by the specified amount.
	 * @param x direction
	 * @param y direction
	 * @param z direction
	 */
	private final void move(float x, float y, float z) {
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
}