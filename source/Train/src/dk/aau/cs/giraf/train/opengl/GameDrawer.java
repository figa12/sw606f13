package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;

/**
 * This class handles all drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadAllTexture()
 */
public final class GameDrawer {

	private abstract class DrawableGroup {
		public abstract void draw();
		public abstract void load();
		
		public void moveAndDraw(Positionable positionable) {
		    for (Coordinate coordinate : positionable.getCoordinates()) {
		        moveTo(coordinate);
		        positionable.draw(gl);
		    }
		}
		
		public void moveAndDraw(Positionable positionable, Color color) {
		    for (Coordinate coordinate : positionable.getCoordinates()) {
                moveTo(coordinate);
                positionable.draw(gl, color);
            }
		}
	}

	private GL10 gl;
	private Context context;
	private float visibleWidth = 1.0f;
	private float visibleHeight = 1.0f;
	private final float DRAWING_DEPTH = -907.744f;
	private float currentX = 0f;
	private float currentY = 0f;
	private float currentZ = 0f;

	private ArrayList<DrawableGroup> gameDrawables = new ArrayList<DrawableGroup>();
	
	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		this.context = context;
		
		// add GameDrawables to the list in the order they should be drawn
		this.addGameDrawable(new Station());
		this.addGameDrawable(new Train());
		this.addGameDrawable(new Wheels());
	}

	public void setVisibleLimits(float visibleWidth, float visibleHeight) {
		this.visibleWidth = visibleWidth;
		this.visibleHeight = visibleHeight;
	}

	private final void addGameDrawable(DrawableGroup gameDrawable) {
		this.gameDrawables.add(gameDrawable);
	}

	/** Draw everything on screen */
	public final void drawGame() {
		this.gl.glLoadIdentity(); // Reset The Current Modelview Matrix

		for (DrawableGroup gameDrawable : this.gameDrawables) {
		    this.resetPosition();
			gameDrawable.draw();
		}
	}

	/** Loads all texture */
	public final void loadAllTexture() {
		for (DrawableGroup gameDrawableTexture : this.gameDrawables) {
			gameDrawableTexture.load();
		}
	}
	
	private final void moveTo(Coordinate coordinate) {
	    this.gl.glTranslatef(coordinate.x - this.currentX, coordinate.y - this.currentY, coordinate.z - this.currentZ);
	    
	    this.currentX += coordinate.x - this.currentX;
        this.currentY += coordinate.y - this.currentY;
        this.currentZ += coordinate.z - this.currentZ;
	}
	
	private final void resetPosition() {
	    this.gl.glLoadIdentity();
	    this.currentX = 0f;
	    this.currentY = 0f;
	    this.currentZ = 0f;
	}
	
	private final class Station extends DrawableGroup {
		private Texture trainStation = new Texture(1.0f, 1.0f);
		
		public Station() {
		    trainStation.addCoordinate(-588.64f, 376f, DRAWING_DEPTH);
		}
		
		@Override
		public void draw() {
			super.moveAndDraw(this.trainStation);
		}

		@Override
		public void load() {
			this.trainStation.loadTexture(gl, context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
			
		}
	}

	private final class Train extends DrawableGroup {

		private Texture train = new Texture(1.0f, 1.0f);
		private Texture wagon = new Texture(1.0f, 1.0f);
		private Square shaft = new Square(40f, 3f);
		
		public Train() {
		    this.wagon.addCoordinate(-542.32f, -142.72f, DRAWING_DEPTH);
		    this.wagon.addCoordinate(-187.45f, -142.72f, DRAWING_DEPTH);
		    this.shaft.addCoordinate(-227.45f, -294.72f, DRAWING_DEPTH);
		    this.shaft.addCoordinate(127.42f, -294.72f, DRAWING_DEPTH);
		    this.train.addCoordinate(160.42f, -52.37f, DRAWING_DEPTH);
		}
		
		@Override
		public void draw() {
			super.moveAndDraw(this.shaft, new Color(0f, 0f, 0f, 1f));
			super.moveAndDraw(this.wagon);
			super.moveAndDraw(this.train);
	}

		@Override
		public void load() {
			this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
			this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
		}
	}
	
	private final class Wheels extends DrawableGroup {
		
		private Texture largeWheel = new Texture(1.0f, 1.0f); // wheel diameter 106.4
		private Texture mediumWheel = new Texture(1.0f, 1.0f); // wheel diameter 78.71
		private Texture smallWheel = new Texture(1.0f, 1.0f); // wheel diameter 60.8
		private Texture wheelShaft = new Texture(1.0f, 1.0f);
		private Texture ground = new Texture(1280.0f, 21.0f);
		
		public Wheels() {
		    this.mediumWheel.addCoordinate(-507.08f, -277.04f, DRAWING_DEPTH);
		    this.mediumWheel.addCoordinate(-339.52f, -277.04f, DRAWING_DEPTH);
		    this.mediumWheel.addCoordinate(-152.14f, -277.04f, DRAWING_DEPTH);
		    this.mediumWheel.addCoordinate(15.42f, -277.04f, DRAWING_DEPTH);
            this.largeWheel.addCoordinate(191.02f, -250.74f, DRAWING_DEPTH);
            this.smallWheel.addCoordinate(344.58f, -296.34f, DRAWING_DEPTH);
            this.smallWheel.addCoordinate(424.13f, -296.34f, DRAWING_DEPTH);
            this.wheelShaft.addCoordinate(380.96f, -338.82f, DRAWING_DEPTH);
            this.ground.addCoordinate(-640f, -356f, DRAWING_DEPTH);
		}
		
		@Override
		public void draw() {
			
		    super.moveAndDraw(this.mediumWheel);
		    super.moveAndDraw(this.largeWheel);
		    super.moveAndDraw(this.smallWheel);
		    super.moveAndDraw(this.wheelShaft);
		    super.moveAndDraw(this.ground);
		}

		@Override
		public void load() {
			this.mediumWheel.loadTexture(gl, context, R.drawable.texture_wheel_medium, Texture.AspectRatio.BitmapOneToOne);
			this.largeWheel.loadTexture(gl, context, R.drawable.texture_wheel_large, Texture.AspectRatio.BitmapOneToOne);
			this.smallWheel.loadTexture(gl, context, R.drawable.texture_wheel_small, Texture.AspectRatio.BitmapOneToOne);
			this.wheelShaft.loadTexture(gl, context, R.drawable.texture_wheel_shaft, Texture.AspectRatio.BitmapOneToOne);
			this.ground.loadTexture(gl, context, R.drawable.texture_ground_mini);
		}
	}
}