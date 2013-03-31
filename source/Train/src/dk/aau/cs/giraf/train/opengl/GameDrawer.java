package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.util.TimingLogger;

/**
 * This class handles all game drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadTexture()
 */
public final class GameDrawer {
    
    /**
     * An abstract class specifying a group of renderables to be drawn and loaded together.
     * Methods for translating, rotating, and drawing.
     * @author Jesper
     */
	private abstract class RenderableGroup {
	    /** Load everything up. Loading happens one time, before drawing. */
	    public abstract void load();
	    /** Draw the group */
	    public abstract void draw();
		
	    /**
	     * Translate to each of the renderable's coordinates and draw it.
	     * @param renderable is the renderable to be drawn.
	     */
		public final void translateAndDraw(Renderable renderable) {
		    this.translateAndDraw(renderable, Colors.White);
		}
		
		/**
         * Translate to each of the renderable's coordinates and draw it.
         * @param renderable is the renderable to be drawn.
		 * @param color is the overlay to be used.
		 */
		public final void translateAndDraw(Renderable renderable, Color color) {
		    for (Coordinate coordinate : renderable.getCoordinates()) {
                moveTo(coordinate);
                renderable.draw(gl, coordinate, color);
            }
		}
		
		/**
		 * Translate to each of the shape's coordinates, rotate, and draw it.
		 * @param angle is the amount to rotate.
		 * @param shape is the shape to be rotated/drawn
		 */
		public final void translateRotateAndDraw(float angle, Shape shape) {
		    this.translateRotateAndDraw(angle, shape, Colors.White);
		}
		
		/**
		 * Translate to each of the shape's coordinates, rotate, and draw it.
		 * @param angle is the amount to rotate.
		 * @param shape is the shape to be rotated/drawn
		 * @param color is the overlay to be used.
		 */
		public final void translateRotateAndDraw(float angle, Shape shape, Color color) {
            for (Coordinate coordinate : shape.getCoordinates()) {
                moveTo(coordinate);
                shape.rotateCenterAndDraw(gl, coordinate, angle, color);
            }
        }
	}

	private GL10 gl;
	private Context context;
	
	private final float FOREGROUND = -907.744f;
	private final float MIDDLEGROUND = -1300f;
	
	private float timeDifference;
	private long systemTimeLast = System.nanoTime();
	private long systemTimeNow = 1;
	
	private Coordinate currentPosition = new Coordinate(0f, 0f, 0f);
	
	/** The list of {@link RenderableGroup}s. */
	private ArrayList<RenderableGroup> gameRenderableGroups = new ArrayList<RenderableGroup>();
	
	/**
	 * Create the {@link GameDrawer}. All {@link RenderableGroup}s are created here.
	 * @param gl the {@link GL10} instance
	 * @param context
	 */
	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		this.context = context;
		
		// add RenderableGroups to the list in the order they should be drawn
		//this.addRenderableGroup(new Middleground());
		this.addRenderableGroup(new Station());
		this.addRenderableGroup(new Train());
		this.addRenderableGroup(new Wheels());
		
		this.addRenderableGroup(new Tester()); // Always draw last
	}
	
	private final void addRenderableGroup(RenderableGroup renderableGroup) {
		this.gameRenderableGroups.add(renderableGroup);
	}

	/** Draw everything on screen. */
	public synchronized final void drawGame() { //FIXME does it give sense to use 'synchronized' keyword here?
	    this.resetPosition();
	    
	    this.systemTimeNow = System.nanoTime();
	    this.timeDifference = (this.systemTimeNow - this.systemTimeLast)/1000000.0f; //FIXME follow the trace of this for the first drawn frame. It's dangerous.
	    
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
	private final void moveTo(Coordinate coordinate) {
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
	
	private final class Station extends RenderableGroup {
		
	    private Texture trainStation = new Texture(1.0f, 1.0f);
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    trainStation.addCoordinate(-588.64f, 376f, FOREGROUND);
		    
		    //Load the textures
            this.trainStation.loadTexture(gl, context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
        }
		
		@Override
		public final void draw() {
			super.translateAndDraw(this.trainStation);
		}
	}

	private final class Train extends RenderableGroup {

		private Texture train = new Texture(1.0f, 1.0f);
		private Texture wagon = new Texture(1.0f, 1.0f);
		private Square shaft = new Square(40f, 3f);
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    this.wagon.addCoordinate(-542.32f, -142.72f, FOREGROUND);
            this.wagon.addCoordinate(-187.45f, -142.72f, FOREGROUND);
            this.shaft.addCoordinate(-227.45f, -294.72f, FOREGROUND);
            this.shaft.addCoordinate(127.42f, -294.72f, FOREGROUND);
            this.train.addCoordinate(160.42f, -52.37f, FOREGROUND);
            
            //Load the textures
            this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
            this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
        }
		
		@Override
		public final void draw() {
			super.translateAndDraw(this.shaft, Colors.Black);
			super.translateAndDraw(this.wagon);
			super.translateAndDraw(this.train);
	}

		
	}
	
	private final class Wheels extends RenderableGroup {
		
		private Texture largeWheel = new Texture(1.0f, 1.0f); // wheel diameter 106.4
		private Texture mediumWheel = new Texture(1.0f, 1.0f); // wheel diameter 78.71
		private Texture smallWheel = new Texture(1.0f, 1.0f); // wheel diameter 60.8
		private Texture wheelShaft = new Texture(1.0f, 1.0f);
		private Texture ground = new Texture(1280.0f, 21.0f);
		
		private Float rotation = 0f;
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    this.mediumWheel.addCoordinate(-507.08f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(-339.52f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(-152.14f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(15.42f, -277.04f, FOREGROUND);
            this.largeWheel.addCoordinate(191.02f, -250.74f, FOREGROUND);
            this.smallWheel.addCoordinate(344.58f, -296.34f, FOREGROUND);
            this.smallWheel.addCoordinate(424.13f, -296.34f, FOREGROUND);
            //this.wheelShaft.addCoordinate(380.96f, -338.82f, DRAWING_DEPTH);
            this.wheelShaft.addCoordinate(370.83f, -321.84f, FOREGROUND);
            this.ground.addCoordinate(-640f, -356f, FOREGROUND);
		    
            //Load the textures
            this.mediumWheel.loadTexture(gl, context, R.drawable.texture_wheel_medium, Texture.AspectRatio.BitmapOneToOne);
            this.largeWheel.loadTexture(gl, context, R.drawable.texture_wheel_large, Texture.AspectRatio.BitmapOneToOne);
            this.smallWheel.loadTexture(gl, context, R.drawable.texture_wheel_small, Texture.AspectRatio.BitmapOneToOne);
            this.wheelShaft.loadTexture(gl, context, R.drawable.texture_wheel_shaft, Texture.AspectRatio.BitmapOneToOne);
            this.ground.loadTexture(gl, context, R.drawable.texture_ground_mini);
        }
		
		@Override
		public final void draw() {
		    super.translateRotateAndDraw(rotation, this.mediumWheel);
		    super.translateRotateAndDraw(rotation, this.largeWheel);
            super.translateRotateAndDraw(rotation, this.smallWheel);
            
            // draw the wheel shaft
            moveTo(this.wheelShaft.getCoordinates().get(0));
            gl.glPushMatrix();
            gl.glRotatef(this.rotation, 0f, 0f, 1f);
            gl.glTranslatef(20f, 0f, 0f);
            gl.glRotatef(-this.rotation, 0f, 0f, 1f);
            this.wheelShaft.draw(gl, currentPosition);
            gl.glPopMatrix();
            
            super.translateAndDraw(this.ground);
		    
		    this.rotation -= 0.2f * timeDifference;
		}
	}
	
	private final class Middleground extends RenderableGroup {
	    
	    private RenderableMatrix sequence = new RenderableMatrix();
	    private Square square = new Square(100f, 100f);
	    
        @Override
        public void load() {
            this.sequence.addCoordinate(500f, 300f, MIDDLEGROUND);
            
            for (float i = 0f; i <= 5000f; i += 100f) {
                this.sequence.addRenderableMatrixItem(square, new Coordinate(i, 0f, 0f), Colors.Blue);
                i += 100f;
                this.sequence.addRenderableMatrixItem(square, new Coordinate(i, 0f, 0f), Colors.Green);
            }
        }

        @Override
        public void draw() {
            super.translateAndDraw(this.sequence);
            
            this.sequence.move(-timeDifference * 0.1f, 0f, 0f);
        }
	}
	
	private final class WeatherGenerator extends RenderableGroup {
	    
	    private Texture bigCloud = new Texture(1f, 1f);
	    private Texture smallCloud = new Texture(1f, 1f);
        private Square overlay = new Square(1280f, 752f);
	    
        @Override
        public void load() {
            // TODO Auto-generated method stub
        }

        @Override
        public void draw() {
            // TODO Auto-generated method stub
            
        }
	    
	}
	
	private final class Tester extends RenderableGroup {
	    
	    /* Thin squares to be used as the axes on screen.
	     * It makes it easier to 'understand' the OpenGL matrix. */
	    private Square horizontalAxis = new Square(1280f, 1.1f);
	    private Square verticalAxis = new Square(1.1f, 752f);
	    
        @Override
        public void load() {
            this.horizontalAxis.addCoordinate(-this.horizontalAxis.getWidth()/2f, this.horizontalAxis.getHeight()/2f, FOREGROUND);
            this.verticalAxis.addCoordinate(-this.verticalAxis.getWidth()/2f, this.verticalAxis.getHeight()/2f, FOREGROUND);
        }

        @Override
        public void draw() {
            super.translateAndDraw(this.horizontalAxis, Colors.TransparentBlack);
            super.translateAndDraw(this.verticalAxis, Colors.TransparentBlack);
        }
	}
}