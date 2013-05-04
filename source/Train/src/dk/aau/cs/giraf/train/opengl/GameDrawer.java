package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.opengl.game.*;

/**
 * This class handles all game drawing.
 * 
 * @author Jesper Riemer Andersen
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadGame()
 */
public final class GameDrawer {
    
	private GL10 gl;
	private GameData gameData;
	public Coordinate currentPosition = new Coordinate(0f, 0f, 0f);
	private Random random = new Random();
	
	/** The list of {@link GameDrawable}s. */
	private ArrayList<GameDrawable> gameDrawables;
	
	/**
	 * Create the {@link GameDrawer}. All {@link GameDrawable}s are created here.
	 * @param gl the {@link GL10} instance.
	 * @param context
	 */
	public GameDrawer(Context context, GL10 gl, GameData gameData) {
		this.gl = gl;
		this.gameData = gameData;
	}
	
	/** Initialises the list of game drawables. */
	public final void initiaslise(Context context) {
	    this.gameDrawables = new ArrayList<GameDrawable>();
	    
	    //Start by creating the stations object, and calculate the stopping positions
        Station station = new Station(gl, context, this, this.gameData);
        station.calculateStoppingPositions();
        
        Train train = new Train(gl, context, this, this.gameData);
        
        // add GameDrawables to the list in the order they should be drawn
        this.gameDrawables.add(new Weather(gl, context, this, this.gameData));
        this.gameDrawables.add(new Clouds(gl, context, this, this.gameData));
        this.gameDrawables.add(new Middleground(gl, context, this, this.gameData));
        this.gameDrawables.add(station);
        this.gameDrawables.add(new TrainDepot(gl,context, this, this.gameData, TrainDepot.BEFORE_TRAIN));
        this.gameDrawables.add(train);
        this.gameDrawables.add(new TrainSmoke(gl, context, this, this.gameData));
        this.gameDrawables.add(new Wheels(gl, context, this, this.gameData));              
        this.gameDrawables.add(new TrainDepot(gl,context, this, this.gameData, TrainDepot.AFTER_TRAIN));
        this.gameDrawables.add(new Overlay(gl, context, this, this.gameData));
        
        this.gameData.bindGameDrawables(station, train);
	}
	
	/** Destroys all the game drawables. Must be initialised again.
	 *  @see GameDrawer#initiaslise(Context) */
	public void freeMemory() {
	    this.gameDrawables = null;
	}
	
	/** Draw everything on screen. */
	public synchronized final void drawGame() {
	    this.resetPosition();
	    
	    this.gameData.systemTimeNow = System.nanoTime();
	    
	    this.gameData.updateData();
	    
		for (int i = 0; i < this.gameDrawables.size(); i++) {
		    this.gameDrawables.get(i).draw();
        }
		
		this.gameData.systemTimeLast = this.gameData.systemTimeNow;
	}

	/** Call all {@link GameDrawable#load()} from {@link GameDrawer#gameDrawables}. */
	public final void loadGame() {
		for (GameDrawable gameDrawable : this.gameDrawables) {
			gameDrawable.load();
		}
		
		//Perform debug 'safety' check:
        this.gameData.performStoppingPositionsCheck();
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
	public final float getRandomNumber(float minimum, float maximum) {
	    return minimum + (maximum - minimum) * this.random.nextFloat();
	}
	
	/**
     * Gets a random number between the specified boundaries.
     * @param minimum is the minimum value of the random number.
     * @param maximum is the maximum value of the random number.
     * @return A random integer between minimum and maximum.
     * @see Random
     */
	public final int getRandomNumber(int minimum, int maximum) {
		return this.random.nextInt(maximum - minimum + 1) + minimum;
	}
}
